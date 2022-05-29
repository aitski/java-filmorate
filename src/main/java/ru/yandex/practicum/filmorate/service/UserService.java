package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        validateId(userId);
        validateId(friendId);
        //add each other to Sets
        getFriendsSet(userId).add(friendId);
        getFriendsSet(friendId).add(userId);
        log.debug("user {} added {} to friends", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        validateId(userId);
        validateId(friendId);
        getFriendsSet(userId).remove(friendId);
        getFriendsSet(friendId).remove(userId);
        log.debug("user {} deleted {} from friends", userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {

        validateId(userId);
        Set<Long> friendsSet = getFriendsSet(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : friendsSet) {
            friends.add(userStorage.findAll().get(id));
        }
        return friends;
    }

    public List<User> getCommonFriendsList(Long userId, Long otherId) {

        List<User> commonFriends = new ArrayList<>();
        validateId(userId);
        validateId(otherId);
        Set<Long> userSet = getFriendsSet(userId);
        Set<Long> otherSet = getFriendsSet(otherId);

        for (Long id : userSet) {
            if (otherSet.contains(id)) {
                commonFriends.add(userStorage.findAll().get(id));
            }
        }
        return commonFriends;
    }

    public User getById(Long id) {
        validateId(id);
        return userStorage.findAll().get(id);
    }

    public Set<Long> getFriendsSet(Long userId) {

        User user = userStorage.findAll().get(userId);
        return user.getFriends();
    }

    public void validateId (Long id){
        if(!userStorage.findAll().containsKey(id)){
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

}
