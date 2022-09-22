package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.comparator.FilmsComparator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public Optional<Film> get(int filmId) {
        return Optional.of(films.get(filmId));
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        Optional<Film> film = get(filmId);
        if(film.isEmpty()) {
            return null;
        }
        var likedFilm = film.get();
        likedFilm.getLikes().add(userId);
        return likedFilm;
    }

    @Override
    public Film unlikeFilm(int filmId, int userId) {
        Optional<Film> film = get(filmId);
        if(film.isEmpty()) {
            return null;
        }
        var unlikedFilm = film.get();
        unlikedFilm.getLikes().add(userId);
        return unlikedFilm;
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.values().stream()
                .sorted(new FilmsComparator())
                .limit(count)
                .collect(Collectors.toList());
    }
}
