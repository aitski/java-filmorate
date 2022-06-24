package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.film.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap();
    UserStorage userStorage = new InMemoryUserStorage();

    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            ObjectNotFoundException v = new ObjectNotFoundException("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
        return films.get(id);
    }
    @Override
    public void addLike(Long filmId, Long userId) {
        getLikesSet(filmId, userId).add(userId);
        log.debug("user {} added like for film {}", userId, filmId);
    }
    @Override
    public void deleteLike(Long filmId, Long userId) {
        getLikesSet(filmId, userId).remove(userId);
        log.debug("user {} deleted like for film {}", userId, filmId);

    }
    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (films.isEmpty()) {
            ObjectNotFoundException v = new ObjectNotFoundException("no films in database");
            log.debug(v.getMessage());
            throw v;
        }

        return findAll().stream().sorted(
                        (p0, p1) -> Integer.compare((p0.getLikes().size()), p1.getLikes().size()) * -1)
                .limit(count).collect(Collectors.toList());
    }

    public Set<Long> getLikesSet(Long filmId, Long userId) {
        if (!films.containsKey(filmId)) {
            ObjectNotFoundException v = new ObjectNotFoundException("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }

        if (userStorage.getById(userId)==null) {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }

        Film film = films.get(filmId);
        return film.getLikes();
    }

    @Override
    public Film save(Film film) {
        validateReleaseDate(film);
        film.setId(Film.counter++);
        films.put(film.getId(), film);
        log.debug("new film created: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {

        if (!films.containsKey(film.getId())) {
            ObjectNotFoundException v = new ObjectNotFoundException("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
        validateReleaseDate(film);
        films.put(film.getId(), film);
        log.debug("film updated: {}", film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public void validateReleaseDate(Film film) {

        LocalDate releaseDate = LocalDate.parse(film.getReleaseDate());
        if (releaseDate.isBefore
                (LocalDate.of(1895, 12, 28))) {
            Validation v = new Validation("check release date");
            log.debug(v.getMessage());
            throw v;
        }
    }

}
