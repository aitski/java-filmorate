package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.Validation;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genres;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenresDbStorage genresDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenresDbStorage genresDbStorage,
                         MpaDbStorage mpaDbStorage,
                         UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresDbStorage = genresDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Film save(Film film) {
        System.out.println("Film save " + film);
        validateReleaseDate(film);

        String sqlQuery = "insert into films (film_description, film_name , " +
                "film_release_date, film_duration,mpa_id,film_rate) " +
                "values (?, ?, ?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement
                    (sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getDescription());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setLong(6, film.getRate());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        if (film.getGenres() != null) {
            Set<Genres> genres = film.getGenres();
            for (Genres genre : genres) {
                genresDbStorage.insertFilmIdToFilmGenres
                        (film.getId(), genre.getId());
            }
        }
        log.debug("film created: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {

        System.out.println("Film update " + film);
        String sqlQuery = "update films set " +
                "film_description = ?, film_name = ?, film_release_date = ?, " +
                "film_duration=?, mpa_id=?, film_rate=? " +
                "where FILM_ID = ?";
        int result = jdbcTemplate.update(sqlQuery
                , film.getDescription()
                , film.getName()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getRate()
                , film.getId());

        if (result == 0) {
            ObjectNotFoundException v = new ObjectNotFoundException
                    ("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }

        if (film.getGenres() != null) {

            Set<Genres> currentGenres = genresDbStorage.getByFilm(film.getId());
            Set<Genres> genres = film.getGenres();

            if (!genres.equals(currentGenres)) {

                for (Genres genre : currentGenres) {
                    genresDbStorage.deleteFilmIdFomFilmGenres
                            (film.getId(), genre.getId());
                }

                for (Genres genre : genres) {
                    genresDbStorage.insertFilmIdToFilmGenres
                            (film.getId(), genre.getId());
                }
            }
        }

        log.debug("film updated: {}", film);
        return film;
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

    @Override
    public List<Film> findAll() {

        String sqlQuery = "select * from films";
        List<Film> list = jdbcTemplate.query(sqlQuery, this::rowToObject);
        log.debug("list of films returned: {}", list);
        return list;
    }

    @Override
    public Film getById(Long id) {

        System.out.println("Film getById " + id);
        SqlRowSet row = jdbcTemplate.queryForRowSet
                ("select * from films where FILM_ID = ?", id);
        Set<Genres> genres = genresDbStorage.getByFilm(id);

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

    @Override
    public void addLike(Long filmId, Long userId) {

        System.out.println("Add like " + filmId + " " + userId);
        userDbStorage.validateId(userId);
        validateId(filmId);
        String sql = "insert into likes (film_id, user_id) " +
                "values(?,?); " +
                "update films set film_rate=film_rate+1 where film_id=?;";
        jdbcTemplate.update(sql, filmId, userId, filmId);
        log.info("Добавлен лайк фильм: {} юзер: {}", filmId, userId);

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

        System.out.println("Remove like " + filmId + " " + userId);
        userDbStorage.validateId(userId);
        validateId(filmId);
        String sql = "delete from likes where film_id=? and user_id = ?; " +
                "update films set film_rate=film_rate-1 where film_id=?;";
        jdbcTemplate.update(sql, filmId, userId, filmId);
        log.info("Удален лайк фильм: {} юзер: {}", filmId, userId);

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {

        String sql = "select * from films order by film_rate desc limit ?";

        List<Film> list = jdbcTemplate.query(sql, this::rowToObject, count);
        log.debug("list of {} popular films returned: {}", count, list);
        return list;
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

    public void validateId(Long filmId) {
        if (getById(filmId) == null) {
            ObjectNotFoundException v = new ObjectNotFoundException
                    ("film does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }


}


