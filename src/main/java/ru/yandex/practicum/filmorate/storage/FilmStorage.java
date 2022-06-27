package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genres;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film save(Film film) ;
    Film update(Film film);
    Set<Genres> getGenresByFilm(Long id);

}
