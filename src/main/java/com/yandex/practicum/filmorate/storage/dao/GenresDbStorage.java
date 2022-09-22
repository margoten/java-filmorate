package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.storage.GenresStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("genresDbStorage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        String select = "SELECT * FROM genres";
        return jdbcTemplate.query(select, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Optional<Genre> get(int id) {
        String select = "SELECT * FROM genres WHERE id = ?";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(select, id);
        if (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getInt("id"),
                    genreRow.getString("name"));

            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}

