package ru.yandex.practicum.filmorate.controllers.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Genres;
import ru.yandex.practicum.filmorate.service.film.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")

public class GenresController {

    private final GenreService genreService;

    @Autowired
    public GenresController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genres> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{genreId}")
    public Genres getById(@PathVariable Long genreId) {
        return genreService.getById(genreId);
    }

}
