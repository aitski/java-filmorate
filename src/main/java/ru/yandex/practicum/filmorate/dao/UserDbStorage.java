package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sqlQuery = "insert into users (user_email, user_login, user_name, user_birthday) " +
                "values (?, ?, ?,?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set " +
                "user_email = ?, user_login = ?, user_name = ?, USER_BIRTHDAY=? " +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        log.debug("user updated: {}", user);
        return user;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

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
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getById(Long id) {
        String sqlQuery = "select * from users where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "truncate table users";
        jdbcTemplate.update(sqlQuery);
        log.debug("all users deleted");
    }
    @Override
    public void addFriend(Long userId, Long friendId) {

        String sqlQuery = "insert into friends (user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.debug("user {} added {} to friends", userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {

        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery,userId, friendId);
        log.debug("user {} deleted {} from friends", userId, friendId);
    }

    @Override
    public List<User> getFriendsList(Long userId) {
        String sqlQuery =
                "select u.*  " +
                "from friends as f " +
                "left join users as u on f.friend_id=u.user_id " +
                "where f.user_id=?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser,userId);
    }

    @Override
    public List<User> getCommonFriendsList(Long userId, Long otherId) {
        String sqlQuery =
            "select * " +
            "from " +
                    "(select u.*  " +
                    "from friends as f "+
                    "left join users as u on f.friend_id=u.user_id "+
                    "where f.user_id=? "+
                    "union all "+
                    "select u.*  from friends as f "+
                    "left join users as u on f.friend_id=u.user_id "+
                    "where f.user_id=?) as common "+
            "group by common.USER_EMAIL, common.USER_LOGIN, common.USER_NAME, common.USER_BIRTHDAY "+
            "having count (common.user_ID)>1";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser,userId,otherId);
    }
}
