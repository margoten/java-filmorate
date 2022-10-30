package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.storage.GenresStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("genresDbStorage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        String select = "SELECT * FROM genre";
        return jdbcTemplate.query(select, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Optional<Genre> get(int id) {
        String select = "SELECT * FROM genre WHERE id = ?";
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

    @Override
    public Map<Integer, Genre> getGenres(List<Integer> ids) {
        String in = String.join(",", Collections.nCopies(ids.size(), "?"));
        String select = String.format("SELECT * FROM genre WHERE id IN (%s)", in);
        return jdbcTemplate.query(select, (rs, rowNum) -> makeGenre(rs), ids.toArray())
                .stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
    }

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        String insert = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(insert, filmId, genreId);
    }

    @Override
    public void removeFilmGenre(int filmId, int genreId) {
        String delete = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?";
        jdbcTemplate.update(delete, filmId, genreId);
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        String select = "SELECT * \n" +
                "FROM genre \n" +
                "INNER JOIN film_genre ON film_genre.genre_id = genre.id\n" +
                "AND film_genre.film_id = ?";
        return jdbcTemplate.query(select, (rs, rowNum) -> makeGenre(rs), filmId);
    }


    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}

