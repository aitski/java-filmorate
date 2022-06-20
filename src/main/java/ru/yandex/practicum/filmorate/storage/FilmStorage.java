package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    void save(Film film) ;
    void deleteAll() ;
    Film update(Film film);
    List<Film> findAll();
    Map<Long,Film> getFilms();

}
