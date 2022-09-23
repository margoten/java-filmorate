package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.storage.MpaStorage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("mpaDbStorage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getMpa() {
        String select = "SELECT * FROM mpa";
        return jdbcTemplate.query(select, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Optional<Mpa> get(int id) {
        String select = "SELECT * FROM mpa WHERE id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(select, id);
        if (mpaRow.next()) {
            Mpa mpa = new Mpa(
                    mpaRow.getInt("id"),
                    mpaRow.getString("name"));

            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int getFilmMpa(int filmId) {
        String select = "SELECT mpa.id \n" +
                "FROM mpa \n" +
                "INNER JOIN film_mpa ON film_mpa.mpa_id = mpa.id\n" +
                "AND film_mpa.film_id = ?";
        return jdbcTemplate.queryForObject(select, (rs, rowNum) -> rs.getInt("id"), filmId);
    }

    @Override
    public void setFilmMpa(int filmId, int mpaId) {
        String insert = "INSERT INTO film_mpa (film_id, mpa_id) VALUES ( ?, ?)";
        jdbcTemplate.update(insert, filmId, mpaId);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("id"), rs.getString("name"));
    }
}
