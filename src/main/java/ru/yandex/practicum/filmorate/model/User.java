package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    public static long counter=1;
    private long id;
    private Set<Long> friends = new HashSet<>();
    @NotBlank
    @Email
    private  String email;
    @NotBlank
    private  String login;
    @NotNull
    private  String name;
    private String birthday;

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
