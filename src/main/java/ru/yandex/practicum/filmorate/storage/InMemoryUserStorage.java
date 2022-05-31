package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap();

    @Override
    public Map<Long, User> getUsers() {
        return users;
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

    @Override
    public void deleteAll() {
        users.clear();
        User.counter=1;
        log.debug("all users deleted");
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
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



