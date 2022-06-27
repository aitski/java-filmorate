package ru.yandex.practicum.filmorate.model.film;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode

public class Film {

    public static long counter = 1;
    private long id;
    private Set<Long> likes = new HashSet<>();
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    private String releaseDate;
    @Positive
    private int duration;
    Set<Genres> genres;
    @NonNull
    Mpa mpa;
    int rate;


    public Film(String name, String description, String releaseDate, int duration, Mpa mpa, int rate) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
    }
}
