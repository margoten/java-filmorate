package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film create(Film film);

    Film update(Film film);

    Film get(int filmId);
}