package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap();

    @Override
    public Map<Long, Film> getFilms() {
        return films;
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
    public void deleteAll () {

        films.clear();
        Film.counter=1;
        log.debug("all films deleted");
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
