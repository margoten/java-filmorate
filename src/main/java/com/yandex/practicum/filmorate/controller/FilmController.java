package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);


    @GetMapping()
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        log.debug("Создание фильма : {}.", film);

        if (film == null) {
            throw new ValidationException("Фильм не может быть создан.");
        }

        if (!validFilm(film)) {
            throw new ValidationException("Фильм не может быть создан.");
        }

        film.setId(idGenerator.incrementAndGet());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        log.debug("Обновление фильма : {}.", film);

        if (film == null) {
            throw new ValidationException("Фильм не может быть обновлен.");
        }

        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не существует.", film.getId());
            throw new ValidationException("Фильм не существует.");
        }
        if (!validFilm(film)) {
            throw new ValidationException("Фильм не может быть обновлен.");
        }
        films.put(film.getId(), film);
        return film;
    }

    public boolean validFilm(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Название фильма пустое.");
            return false;
        }

        if (film.getDescription().length() >= 200) {
            log.warn("Описание фильма слишком длинная. Максимальная длина - 200 символов.");
            return false;
        }


        if (LocalDate.parse(film.getReleaseDate(), Util.DATE_FORMAT).isBefore(CINEMA_BIRTHDAY)) {
            log.warn("Релиз фильма раньше {}.", CINEMA_BIRTHDAY.format(Util.DATE_FORMAT));
            return false;
        }

        if (film.getDuration() <= 0) {
            log.warn("Некорректная продолжительность фильма {}.", film.getDuration());
            return false;
        }
        return true;
    }
}
