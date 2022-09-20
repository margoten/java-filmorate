package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }


    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }


    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(int filmId) {
        return films.get(filmId);
    }
}
