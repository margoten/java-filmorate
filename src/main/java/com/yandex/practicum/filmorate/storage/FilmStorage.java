package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getFilms();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> get(int filmId);

    List<Film> getMostPopularFilms(int count);

    Film likeFilm(int filmId, int userId);

    Film unlikeFilm(int filmId, int userId);
}
