package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.model.comparator.FilmsComparator;
import com.yandex.practicum.filmorate.storage.FilmStorage;
import com.yandex.practicum.filmorate.storage.UserStorage;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MAX_FILM_DESCRIPTION_SIZE = 200;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final int DEFAULT_COUNT_POPULAR_FILMS = 10;
    private int idGenerator = 0;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть создан.");
        }
        validationFilm(film);

        film.setId(generatedId());
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть обновлен.");
        }

        if (filmStorage.get(film.getId()).isEmpty()) {
            log.warn("Фильм с id {} не существует.", film.getId());
            throw new NotFoundException("Фильм не существует.");
        }
        validationFilm(film);
        return filmStorage.update(film);
    }

    public Film getFilm(int id) {
        Optional<Film> film = filmStorage.get(id);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + id + " не существует.");
        }
        return film.get();
    }

    public Film likeFilm(int userId, int filmId) {
        Optional<User> user = userStorage.get(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
        Film film = filmStorage.likeFilm(filmId, userId);
        if(film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не существует.");
        }
        return film;
    }

    public Film unlikeFilm(int userId, int filmId) {
        Optional<User> user = userStorage.get(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }

        Film film = filmStorage.unlikeFilm(filmId, userId);
        if(film == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не существует.");
        }
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public List<Film> getMostPopularFilms(String countStr) {
        int count = DEFAULT_COUNT_POPULAR_FILMS;
        if (countStr != null) {
            count = Integer.parseInt(countStr);
        }
        return filmStorage.getMostPopularFilms(count);
    }

    private void validationFilm(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Название фильма пустое.");
            throw new ValidationException("Название фильма пустое.");
        }

        if (film.getDescription().length() >= MAX_FILM_DESCRIPTION_SIZE) {
            log.warn("Описание фильма слишком длинная. Максимальная длина - 200 символов.");
            throw new ValidationException("Описание фильма слишком длинная. Максимальная длина - 200 символов.");
        }


        if (LocalDate.parse(film.getReleaseDate(), Util.DATE_FORMAT).isBefore(CINEMA_BIRTHDAY)) {
            log.warn("Релиз фильма раньше {}.", CINEMA_BIRTHDAY.format(Util.DATE_FORMAT));
            throw new ValidationException("Некорректная дата выхода фильма");
        }

        if (film.getDuration() <= 0) {
            log.warn("Некорректная продолжительность фильма {}.", film.getDuration());
            throw new ValidationException("Некорректная продолжительность фильма " + film.getDuration() + ".");
        }
    }

    private int generatedId() {
        return ++idGenerator;
    }
}
