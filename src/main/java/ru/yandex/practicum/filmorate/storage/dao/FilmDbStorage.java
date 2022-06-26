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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                insertFilmIdToFilmGenres (film.getId(), genre.getId());
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

            Set<Genres> currentGenres = getGenresByFilm(film.getId());
            Set<Genres> genres = film.getGenres();

            if (!genres.equals(currentGenres)) {

                for (Genres genre : currentGenres) {
                    deleteFilmIdFomFilmGenres(film.getId(), genre.getId());
                }

                for (Genres genre : genres) {
                    insertFilmIdToFilmGenres(film.getId(), genre.getId());
                }
            }
        }

        log.debug("film updated: {}", film);
        return film;
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

    public void insertFilmIdToFilmGenres(Long filmId, Long genreId) {
        String sql = "insert into film_genres (film_id, genre_id) " +
                "values(?,?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    public void deleteFilmIdFomFilmGenres(Long filmId, Long genreId) {
        String sql = "delete from film_genres where film_id=? and genre_id = ?;";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    public Set<Genres> getGenresByFilm(Long filmId) {
        String sqlQuery = "select g.genre_id, g.genre_name " +
                "from film_genres as f " +
                "left join genres as g on g.genre_id = f.genre_id " +
                "where f.FILM_ID=? order by g.genre_id asc";

        Collection<Genres> list = jdbcTemplate.query(sqlQuery, this::genresRowToObject, filmId);
        log.debug("list of Genres returned: {}", list);

        if (list.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(list);
    }

    private Genres genresRowToObject(ResultSet resultSet, int rowNum) throws SQLException {

        Genres genre = new Genres(
                resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));
        return genre;
    }

}


