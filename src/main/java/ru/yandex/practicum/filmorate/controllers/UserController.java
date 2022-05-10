package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap();

    @PostMapping
    public User saveUser(@Valid @RequestBody User user) throws Validation {

        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        user.setId(User.counter++);
        users.put(user.getId(), user);
        log.debug("new user created: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws Validation {

        validateBirthday(user);
        validateLogin(user);
        validateName(user);
        users.put(user.getId(), user);
        log.debug("user updated: {}", user);
        return user;
    }

    @GetMapping
    public Map<Integer, User> findAll() {
        return users;
    }

    public void validateName(User user) {

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("user blank name was replaced with login: {}", user.getName());
        }
    }

    public void validateBirthday(User user) throws Validation {
        LocalDate birthday = LocalDate.parse(user.getBirthday());

        if (birthday.isAfter(LocalDate.now())) {
            Validation v = new Validation("check birthday");
            log.debug(v.getMessage());
            throw v;
        }
    }
    public void validateLogin(User user) throws Validation {

        if (user.getLogin().contains(" ")){
            Validation v = new Validation("check login");
            log.debug(v.getMessage());
            throw v;
        }
    }
}