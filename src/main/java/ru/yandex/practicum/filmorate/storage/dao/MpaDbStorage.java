package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa rowToObject(ResultSet resultSet, int rowNum) throws SQLException {

        Mpa mpa = new Mpa(
                resultSet.getLong("mpa_id"),
                resultSet.getString("mpa_name"));
        return mpa;
    }

    public List<Mpa> findAll() {
        System.out.println("Mpa findAll");
        String sqlQuery = "select * from mpa";
        List<Mpa> list = jdbcTemplate.query(sqlQuery, this::rowToObject);
        log.debug("list of Mpa returned: {}", list);
        return list;
    }

    public Mpa getById(Long id) {
        System.out.println("Mpa getById " + id);
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select * from mpa where MPA_ID = ?", id);

        if (rows.next()) {
            Mpa mpa = new Mpa(
                    rows.getLong("mpa_id"),
                    rows.getString("mpa_name"));
            log.info("Найден mpa: {} {}", mpa.getId(), mpa.getName());
            return mpa;
        } else {
            ObjectNotFoundException v = new ObjectNotFoundException("mpa does not exist in database");
            log.debug(v.getMessage());
            throw v;
        }
    }

}

