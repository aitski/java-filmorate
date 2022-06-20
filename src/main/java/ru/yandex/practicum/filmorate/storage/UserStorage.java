package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    
    void save(User user) ;
    void deleteAll() ;
    User update(User user) ;
    List<User> findAll();
    User getById (Long id);
    void addFriend (Long userId, Long friendId);
    void deleteFriend(Long userId, Long friendId);
    List<User> getFriendsList(Long userId);
    List<User> getCommonFriendsList(Long userId, Long otherId);

}
