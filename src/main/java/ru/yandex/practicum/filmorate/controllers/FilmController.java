package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {

    private Map<Integer, Film> films = new HashMap();

    @Override
    @PostMapping
    public Film save(@Valid @RequestBody Film film) throws Validation {
        validateReleaseDate(film);
        film.setId(Film.counter++);
        films.put(film.getId(), film);
        log.debug("new film created: {}", film);
        return film;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws Validation {
        validateReleaseDate(film);
        films.put(film.getId(), film);
        log.debug("film updated: {}", film);
        return film;
    }

    @Override
    @GetMapping
    public Map<Integer, Film> findAll() {
        return films;
    }

    @Override
    @DeleteMapping
    public void deleteAll() {
        films.clear();
        Film.counter = 0;
    }

    public void validateReleaseDate(Film film) throws Validation {

        LocalDate releaseDate = LocalDate.parse(film.getReleaseDate());
        if (releaseDate.isBefore
                (LocalDate.of(1895, 12, 28))) {
            Validation v = new Validation("check release date");
            log.debug(v.getMessage());
            throw v;
        }
    }
}
