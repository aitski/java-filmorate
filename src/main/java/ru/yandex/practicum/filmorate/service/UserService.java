package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void save(User user){
        userStorage.save(user);
    }

    public void deleteAll(){
        userStorage.deleteAll();
    }
    public User update(User user){
        return userStorage.update(user);
    }
    public List<User> findAll(){
        return userStorage.findAll();
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId,friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId,friendId);
    }

    public List<User> getFriendsList(Long userId) {
        return userStorage.getFriendsList(userId);
    }

    public List<User> getCommonFriendsList(Long userId, Long otherId) {
        return userStorage.getCommonFriendsList(userId,otherId);
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

}
