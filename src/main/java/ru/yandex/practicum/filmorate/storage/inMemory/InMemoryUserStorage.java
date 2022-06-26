package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.user.Status;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap();

    @Override
    public User getById(Long id) {
        validateId(id);
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Map<Long, Status> getFriendsMap(Long userId) {

        User user = users.get(userId);
        return user.getFriends();
    }

    @Override
        public User save(User user) {

        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        user.setId(User.counter++);
        users.put(user.getId(), user);
        log.debug("new user created: {}", user);
        return user;
    }

    @Override
       public User update(User user) {

        if(!users.containsKey(user.getId())){
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        users.put(user.getId(), user);
        log.debug("user updated: {}", user);
        return user;
    }


    public void addFriend(Long userId, Long friendId) {
        validateId(userId);
        validateId(friendId);
        //add to friend one-sided
        getFriendsMap(friendId).put(userId,Status.PENDING);
        log.debug("user {} added {} to friends", userId, friendId);
    }


    public void deleteFriend(Long userId, Long friendId) {
        validateId(userId);
        validateId(friendId);
        getFriendsMap(friendId).remove(userId);
        log.debug("user {} deleted {} from friends", userId, friendId);

    }


    public List<User> getFriendsList(Long userId) {
        validateId(userId);
        Map<Long,Status> friendsMap = getFriendsMap(userId);
        List<User> friends = new ArrayList<>();
        for (Long id : friendsMap.keySet()) {
            friends.add(users.get(id));
        }
        return friends;
    }


    public List<User> getCommonFriendsList(Long userId, Long otherId) {
        List<User> commonFriends = new ArrayList<>();
        validateId(userId);
        validateId(otherId);
        Map<Long,Status> userSet = getFriendsMap(userId);
        Map<Long,Status> otherSet = getFriendsMap(otherId);

        for (Long id : userSet.keySet()) {
            if (otherSet.containsKey(id)) {
                commonFriends.add(users.get(id));
            }
        }
        return commonFriends;
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

    public void validateId(Long id) {
        if (!users.containsKey(id)) {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

}



