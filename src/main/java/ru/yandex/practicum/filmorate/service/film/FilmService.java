package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genres;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenresDbStorage genresDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(@Qualifier ("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier ("userDbStorage") UserStorage userStorage,
                       GenresDbStorage genresDbStorage,
                       MpaDbStorage mpaDbStorage,
                       JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genresDbStorage = genresDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public  Film save(Film film){
        return filmStorage.save(film);
    }

    public Film update(Film film){

        return filmStorage.update(film);
    }
    public List<Film> findAll() {

        String sqlQuery = "select * from films";
        List<Film> list = jdbcTemplate.query(sqlQuery, this::rowToObject);
        log.debug("list of films returned: {}", list);
        return list;
    }

    public Film getById(Long id) {

        System.out.println("Film getById " + id);
        SqlRowSet row = jdbcTemplate.queryForRowSet
                ("select * from films where FILM_ID = ?", id);
        Set<Genres> genres = filmStorage.getGenresByFilm(id);

        if (row.next()) {
            Film film = new Film(
                    row.getString("film_name"),
                    row.getString("film_description"),
                    row.getString("film_release_date"),
                    row.getInt("film_duration"),
                    mpaDbStorage.getById(row.getLong("mpa_id")),
                    row.getInt("film_rate"));

            if (!genres.isEmpty()) {
                film.setGenres(genres);
            }

            film.setId(row.getLong("film_id"));
            log.info("Найден film: {} {}", film.getId(), film.getName());
            return film;
        } else {
            ObjectNotFoundException v = new ObjectNotFoundException
                    ("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

    public void addLike(Long filmId, Long userId) {

        System.out.println("Add like " + filmId + " " + userId);
        validateUserId(userId);
        validateFilmId(filmId);
        String sql = "insert into likes (film_id, user_id) " +
                "values(?,?); " +
                "update films set film_rate=film_rate+1 where film_id=?;";
        jdbcTemplate.update(sql, filmId, userId, filmId);
        log.info("Добавлен лайк фильм: {} юзер: {}", filmId, userId);

    }

    public void deleteLike(Long filmId, Long userId) {

        System.out.println("Remove like " + filmId + " " + userId);
        validateUserId(userId);
        validateFilmId(filmId);
        String sql = "delete from likes where film_id=? and user_id = ?; " +
                "update films set film_rate=film_rate-1 where film_id=?;";
        jdbcTemplate.update(sql, filmId, userId, filmId);
        log.info("Удален лайк фильм: {} юзер: {}", filmId, userId);

    }

    public List<Film> getPopularFilms(Integer count) {

        String sql = "select * from films order by film_rate desc limit ?";

        List<Film> list = jdbcTemplate.query(sql, this::rowToObject, count);
        log.debug("list of {} popular films returned: {}", count, list);
        return list;
    }

    public void validateFilmId(Long filmId) {
        if (getById(filmId) == null) {
            ObjectNotFoundException v = new ObjectNotFoundException
                    ("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

    public void validateUserId(Long userId) {
        if (userStorage.getById(userId) == null) {
            ObjectNotFoundException v = new ObjectNotFoundException("user does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

    private Film rowToObject(ResultSet resultSet, int rowNum) throws SQLException {

        Film film = new Film(
                resultSet.getString("film_name"),
                resultSet.getString("film_description"),
                resultSet.getString("film_release_date"),
                resultSet.getInt("film_duration"),
                mpaDbStorage.getById(resultSet.getLong("mpa_id")),
                resultSet.getInt("film_rate"));
        film.setId(resultSet.getLong("film_id"));
        return film;
    }


}






