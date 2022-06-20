package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode

public class User {

    public static long counter=1;
    private long id;
    private Map<Long, Status> friends = new HashMap<>();
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
