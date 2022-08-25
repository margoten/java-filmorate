package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final int MAX_FILM_DESCRIPTION_SIZE = 200;
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public List<Film> getFilms() {
      return List.copyOf(films.values());
    }


    @Override
    public Film create(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть создан.");
        }
        validationFilm(film);
        film.setId(generatedId());
        films.put(film.getId(), film);
        return film;
    }


    @Override
    public Film update(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть обновлен.");
        }

        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не существует.", film.getId());
            throw new NotFoundException("Фильм не существует.");
        }
        validationFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(int filmId) {
        return films.get(filmId);
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
