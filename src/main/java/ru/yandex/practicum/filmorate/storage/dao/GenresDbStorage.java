package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class GenresDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genres rowToObject(ResultSet resultSet, int rowNum) throws SQLException {

        Genres genre = new Genres(
                resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));
        return genre;
    }

    public List<Genres> findAll() {
        System.out.println("Genres findAll");
        String sqlQuery = "select * from genres";
        List<Genres> list = jdbcTemplate.query(sqlQuery, this::rowToObject);
        log.debug("list of Genres returned: {}", list);
        return list;
    }

    public Genres getById(Long id) {
        System.out.println("Genres getById " + id);
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select * from genres where GENRE_ID = ?", id);

        if (rows.next()) {
            Genres genre = new Genres(
                    rows.getLong("genre_id"),
                    rows.getString("genre_name"));
            log.info("Найден genre: {} {}", genre.getId(), genre.getName());
            return genre;
        } else {
            ObjectNotFoundException v = new ObjectNotFoundException("genre does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

    public Set<Genres> getByFilm(Long filmId) {
        String sqlQuery = "select g.genre_id, g.genre_name " +
                "from film_genres as f " +
                "left join genres as g on g.genre_id = f.genre_id " +
                "where f.FILM_ID=? order by g.genre_id asc";

        Collection<Genres> list = jdbcTemplate.query(sqlQuery, this::rowToObject, filmId);
        log.debug("list of Genres returned: {}", list);

        if (list.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(list);
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

}

