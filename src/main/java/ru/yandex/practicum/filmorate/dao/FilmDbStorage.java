package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Map;

public class FilmDbStorage implements FilmStorage {

    @Override
    public void save(Film film) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }
}
