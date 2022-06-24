package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier ("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public  Film save(Film film){
        return filmStorage.save(film);
    }
    public Film getById(Long id) {
        return filmStorage.getById(id);
    }
    public Film update(Film film){
        return filmStorage.update(film);
    }
    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId,userId);
    }
    public void deleteLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId,userId);
    }

    public List<Film> getPopularFilms(Integer count) {
       return filmStorage.getPopularFilms(count);
    }

}






