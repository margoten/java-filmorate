package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Film> getFilms() {
        String select = "SELECT * FROM film";
        return null;
    }

    @Override
    public Film create(Film film) {
        String insert = "INSERT INTO film (id, name, description, release_date, duration) VALUES ( ?, ?, ?, ?,?)";
        return null;
    }

    @Override
    public Film update(Film film) {
        String delete = "DELETE FROM film WHERE id = ?";
        String insert = "INSERT INTO film (id, name, description, release_date, duration) VALUES ( ?, ?, ?, ?,?)";

        return null;
    }

    @Override
    public Film get(int filmId) {
        String select = "SELECT * FROM film WHERE id = ?";
        return null;
    }
}
