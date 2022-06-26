package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user){
        return userStorage.save(user);
    }

    public User update(User user){
        return userStorage.update(user);
    }
    public List<User> findAll(){
        return userStorage.findAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public void addFriend(Long userId, Long friendId) {

        System.out.println("addFriend " + userId + " " + friendId);

        validateId(userId);
        validateId(friendId);

        String sqlQuery = "insert into friends (user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.debug("user {} added {} to friends", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        System.out.println("deleteFriend " + userId + " " + friendId);

        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.debug("user {} deleted {} from friends", userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {
        System.out.println("getFriendsList " + userId);

        validateId(userId);

        String sqlQuery =
                "select u.*  " +
                        "from friends as f " +
                        "left join users as u on f.friend_id=u.user_id " +
                        "where f.user_id=?";
        List<User> list = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
        log.debug("list of friends returned: {}", list);
        return list;
    }

    public List<User> getCommonFriendsList(Long userId, Long otherId) {
        System.out.println("getCommonFriendsList " + userId);

        validateId(userId);
        validateId(otherId);

        String sqlQuery =
                "select * " +
                        "from " +
                        "(select u.*  " +
                        "from friends as f " +
                        "left join users as u on f.friend_id=u.user_id " +
                        "where f.user_id=? " +
                        "union all " +
                        "select u.*  from friends as f " +
                        "left join users as u on f.friend_id=u.user_id " +
                        "where f.user_id=?) as common " +
                        "group by common.USER_EMAIL, common.USER_LOGIN, common.USER_NAME, common.USER_BIRTHDAY " +
                        "having count (common.user_ID)>1";
        List<User> list = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherId);
        log.debug("list of common friends returned: {}", list);
        return list;
    }

    public void validateId(Long userId) {
        if (getById(userId) == null) {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
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


}
