package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.storage.FilmStorage;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("filmDbStorage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenresDbStorage genresDbStorage;


    @Override
    public List<Film> getFilms() {
        String select = "SELECT * FROM film";
        return jdbcTemplate.query(select, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film create(Film film) {
        String insert = "INSERT INTO film (id, name, description, release_date, duration) VALUES ( ?, ?, ?, ?,?)";
        jdbcTemplate.update(insert, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @Override
    public Film update(Film film) {
        String delete = "DELETE FROM film WHERE id = ?";
        String insert = "INSERT INTO film (id, name, description, release_date, duration) VALUES ( ?, ?, ?, ?,?)";

        jdbcTemplate.update(delete, film.getId());
        jdbcTemplate.update(insert, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());

        return film;
    }

    @Override
    public Optional<Film> get(int filmId) {
        String select = "SELECT * FROM film WHERE id = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(select, filmId);
        if (filmRow.next()) {
            Film film = Film.builder()
                    .id(filmRow.getInt("id"))
                    .name(filmRow.getString("name"))
                    .description(filmRow.getString("description"))
                    .duration(filmRow.getInt("duration"))
                    .releaseDate(filmRow.getDate("release_date").toLocalDate())
                    .mpaId(mpaDbStorage.getFilmMpa(filmId))
                    .build();
            film.getGenres().addAll(genresDbStorage.getFilmGenres(filmId));
            film.getLikes().addAll(getUserLikes(filmId));
            return Optional.of(film);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String select = "SELECT f.*" +
                "FROM film AS f " +
                "LEFT OUTER JOIN film_likes AS fl ON f.id = fl.film_id " +
                "GROUP BY f.id, fl.user_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(select, (rs, rowNum) -> makeFilm(rs));
        films.forEach(film -> {
            film.getGenres().addAll(genresDbStorage.getFilmGenres(film.getId()));
            film.getLikes().addAll(getUserLikes(film.getId()));
            film.setMpaId(mpaDbStorage.getFilmMpa(film.getId()));
        });
        return films;
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        String insert = "INSERT INTO film_likes (user_id, film_id) VALUES ( ?, ?)";
        jdbcTemplate.update(insert, userId, filmId);
        return null;
    }

    @Override
    public Film unlikeFilm(int filmId, int userId) {
        String delete = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(delete, userId, filmId);
        return null;
    }

    private List<Integer> getUserLikes(int filmId) {
        String select = "SELECT user_id \n" +
                "FROM film_likes \n" +
                "WHERE film_id = ?";
        return jdbcTemplate.query(select, (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Film film = Film.builder()
                .id(id)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpaId(mpaDbStorage.getFilmMpa(id))
                .build();
        film.getLikes().addAll(getUserLikes(id));
        film.getGenres().addAll(genresDbStorage.getFilmGenres(id));
        return film;

    }
}
