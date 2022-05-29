package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Film save(Film film) ;
    void deleteAll() ;
    Film update(Film film);
    Map<Long, Film> findAll();

}
