package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private static FilmController filmController;

    @BeforeAll
    public static void createController() {
        filmController = new FilmController();
    }

    @Test
    void shouldExceptionWithNull() {
        assertThrows(ValidationException.class, () -> filmController.create(null));
    }

    @Test
    void shouldExceptionWithEmptyName() {
        Film film = new Film(0, "", "descr", "2000-10-10", 10);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldExceptionWithTooLongDescription() {
        Film film = new Film(0, "film", "Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam," +
                " quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "2000-10-10", 100);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldExceptionWithNegativeDuration() {
        Film film = new Film(0, "film", "descr",
                "2000-10-10", -100);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldExceptionWithIncorrectReleaseDay() {
        Film film = new Film(0, "film", "descr",
                "1600-10-10", 100);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        Film film = new Film(4, "film", "updated descr",
                "2000-10-10", 100);
        assertThrows(ValidationException.class, () -> filmController.update(film));
    }
}