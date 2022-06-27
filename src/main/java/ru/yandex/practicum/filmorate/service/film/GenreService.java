package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genres;
import ru.yandex.practicum.filmorate.storage.dao.GenresDbStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenresDbStorage genreStorage;

    @Autowired
    public GenreService(GenresDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genres> findAll() {
        return genreStorage.findAll();
    }

    public Genres getById(Long id) {
        return genreStorage.getById(id);
    }

}
