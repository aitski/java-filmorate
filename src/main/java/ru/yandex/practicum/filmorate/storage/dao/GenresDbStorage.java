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

}

