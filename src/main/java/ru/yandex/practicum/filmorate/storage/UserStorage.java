package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    
    User save(User user) ;
    void deleteAll() ;
    User update(User user) ;
    public Map<Long, User> findAll();

}
