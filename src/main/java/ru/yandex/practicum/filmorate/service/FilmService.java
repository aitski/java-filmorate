package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public  void save(Film film){
        filmStorage.save(film);
    }

    public void deleteAll(){
      filmStorage.deleteAll();
    }
    public Film update(Film film){
        return filmStorage.update(film);
    }
    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public void addLike(Long filmId, Long userId) {

        getLikesSet(filmId, userId).add(userId);
        log.debug("user {} added like for film {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        getLikesSet(filmId, userId).remove(userId);
        log.debug("user {} deleted like for film {}", userId, filmId);
    }

    public List<Film> getPopularFilms(Integer count) {

        if (filmStorage.getFilms().isEmpty()) {
            ObjectNotFoundException v = new ObjectNotFoundException("no films in database");
            log.debug(v.getMessage());
            throw v;
        }

        return filmStorage.findAll().stream().sorted(
                        (p0, p1) -> Integer.compare((p0.getLikes().size()), p1.getLikes().size()) * -1)
                .limit(count).collect(Collectors.toList());
    }

    public Film getById(Long id) {

        if (!filmStorage.getFilms().containsKey(id)) {
            ObjectNotFoundException v = new ObjectNotFoundException("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
        return filmStorage.getFilms().get(id);
    }

    public Set<Long> getLikesSet(Long filmId, Long userId) {

        if (!filmStorage.getFilms().containsKey(filmId)) {
            ObjectNotFoundException v = new ObjectNotFoundException("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
/*
        if (!userStorage.getUsers().containsKey(userId)) {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
*/
        Film film = filmStorage.getFilms().get(filmId);
        return film.getLikes();
    }

}






