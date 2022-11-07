package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface FilmStorage {
    List<Film> getFilms();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> get(int filmId);

    List<Film> getMostPopularFilms(int count);

    void likeFilm(Film film, int userId);

    void unlikeFilm(Film film, int userId);

    TreeSet<Film> getCommonFilms(int userId, int friendId);
}
