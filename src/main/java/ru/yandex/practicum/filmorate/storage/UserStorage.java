package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {
    
    User save(User user) ;
    User update(User user) ;
    List<User> findAll();
    User getById (Long id);
    void addFriend (Long userId, Long friendId);
    void deleteFriend(Long userId, Long friendId);
    List<User> getFriendsList(Long userId);
    List<User> getCommonFriendsList(Long userId, Long otherId);

}
