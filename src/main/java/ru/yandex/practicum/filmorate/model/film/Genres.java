package ru.yandex.practicum.filmorate.model.film;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Genres {
    @EqualsAndHashCode.Include
    private long id;
    private String name;

    public Genres(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
