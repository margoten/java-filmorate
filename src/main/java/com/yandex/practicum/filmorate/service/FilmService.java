package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.FilmStorage;
import com.yandex.practicum.filmorate.storage.UserStorage;
import com.yandex.practicum.filmorate.model.comparator.FilmsComparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final int DEFAULT_COUNT_POPULAR_FILMS = 10;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilm(int id) {
        Film film = filmStorage.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + id + " не существует.");
        }
        return film;
    }

    public Film likeFilm(int userId, int filmId) {
        Film film = filmStorage.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не существует.");
        }
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
        film.getLikes().add(userId);
        return film;
    }

    public Film unlikeFilm(int userId, int filmId) {
        Film film = filmStorage.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не существует.");
        }
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public List<Film> getMostPopularFilms(String countS) {
        int count = DEFAULT_COUNT_POPULAR_FILMS;
        if (countS != null) {
            count = Integer.parseInt(countS);
        }
        log.warn("11111111111111111111111 count = " + count);
        log.warn("11111111111111111111111 films = " + filmStorage.getFilms());
        var sordet =  filmStorage.getFilms().stream()
                .sorted(new FilmsComparator())
                .limit(count)
                .collect(Collectors.toList());
        log.warn("11111111111111111111111 sorted films = " + sordet);
        return sordet;

    }
}
