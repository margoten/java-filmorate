package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.service.FilmService;
import com.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import com.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private static FilmController filmController;
    private Film film;

    @BeforeAll
    public static void createController() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @BeforeEach
    public void createFilm() {
        film = new Film(0, "name", "descr", "2000-10-10", 10, new HashSet<>());
    }

    @Test
    void shouldExceptionWithNull() {
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(null));
        Assertions.assertEquals("Фильм не может быть создан.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyName() {
        film.setName("");
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals("Название фильма пустое.", ex.getMessage());

    }

    @Test
    void shouldExceptionWithTooLongDescription() {
        film.setDescription("d".repeat(201));
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals("Описание фильма слишком длинная. Максимальная длина - 200 символов.", ex.getMessage());

    }

    @Test
    void shouldExceptionWithNegativeDuration() {
       film.setDuration(-100);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals("Некорректная продолжительность фильма -100.", ex.getMessage());

    }

    @Test
    void shouldExceptionWithIncorrectReleaseDay() {
        film.setReleaseDate("1600-10-10");
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals("Некорректная дата выхода фильма", ex.getMessage());

    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        film.setId(4);
        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals("Фильм не существует.", ex.getMessage());

    }
}