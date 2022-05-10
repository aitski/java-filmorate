package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
public class User {

    public static int counter;
    private int id;
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
