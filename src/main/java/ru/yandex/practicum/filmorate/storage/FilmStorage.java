package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

public interface FilmStorage {

    Film save(Film film) ;
    Film update(Film film);
    List<Film> findAll();
    public Film getById(Long id);
    public void addLike(Long filmId, Long userId);
    public void deleteLike(Long filmId, Long userId);
    public List<Film> getPopularFilms(Integer count);
}
