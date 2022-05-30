package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/films")

public class FilmController extends Controller<Film> {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @Override
    @PostMapping
    public Film save(@Valid @RequestBody Film film) {

        return filmService.save(film);

    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @Override
    @DeleteMapping
    public void deleteAll() {
        filmService.deleteAll();
    }

    @Override
    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable Long filmId) {
        return filmService.getById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(
            defaultValue = "10"
            , required = false) Integer count) {

        return filmService.getPopularFilms(count);

    }

}
