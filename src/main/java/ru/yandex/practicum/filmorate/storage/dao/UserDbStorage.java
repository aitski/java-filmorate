package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        System.out.println("User save " + user);

        validateBirthday(user);
        validateLogin(user);
        validateName(user);

        String sqlQuery = "insert into users (user_email, user_login, user_name, user_birthday) " +
                "values (?, ?, ?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getBirthday());
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.debug("user created: {}", user);
        return user;

    }

    @Override
    public User update(User user) {

        System.out.println("User update " + user);
        String sqlQuery = "update users set " +
                "user_email = ?, user_login = ?, user_name = ?, USER_BIRTHDAY=? " +
                "where USER_ID = ?";

        int result = jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());

        if (result == 0) {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }

        log.debug("user updated: {}", user);
        return user;
    }

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        User user = new User(
                resultSet.getString("user_email"),
                resultSet.getString("user_login"),
                resultSet.getString("user_name"),
                resultSet.getString("user_birthday"));
        user.setId(resultSet.getLong("user_id"));
        return user;
    }

    @Override
    public List<User> findAll() {
        System.out.println("Users findAll");
        String sqlQuery = "select * from users";
        List<User> list = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        log.debug("list of users returned: {}", list);
        return list;
    }

    @Override
    public User getById(Long id) {
        System.out.println("User getById " + id);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where USER_ID = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getString("user_email"),
                    userRows.getString("user_login"),
                    userRows.getString("user_name"),
                    userRows.getString("user_birthday"));
            user.setId(userRows.getLong("user_id"));
            log.info("???????????? ????????????????????????: {} {}", user.getId(), user.getName());
            return user;
        } else {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

    public void validateName(User user) {

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("user blank name was replaced with login: {}", user.getName());
        }
    }

    public void validateBirthday(User user) {
        LocalDate birthday = LocalDate.parse(user.getBirthday());

        if (birthday.isAfter(LocalDate.now())) {
            Validation v = new Validation("birthday is in future");
            log.debug(v.getMessage());
            throw v;
        }
    }

    public void validateLogin(User user) {

        if (user.getLogin().contains(" ")) {
            Validation v = new Validation("login contains spaces");
            log.debug(v.getMessage());
            throw v;
        }
    }

}
