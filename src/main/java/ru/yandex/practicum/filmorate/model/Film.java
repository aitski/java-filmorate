package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class Film {

    public static int counter;
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max=200)
    private String description;
    private String releaseDate;
    @Positive
    private int duration;

    public Film(String name, String description, String releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
