package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private static UserController userController;

    @BeforeAll
    public static void createController() {
        userController = new UserController();
    }

    @Test
    void shouldExceptionWithNull() {
        assertThrows(ValidationException.class, () -> userController.create(null));
    }

    @Test
    void shouldExceptionWithEmptyLogin() {
        User user = new User(0, "sasadas@dfsdfd.com", "", "name", "2000-10-10");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldExceptionWithIncorrectLogin() {
        User user = new User(0, "sasadas@dfsdfd.com", "l o g i n", "name", "2000-10-10");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldExceptionWithEmptyEmail() {
        User user = new User(0, "", "login", "name", "2000-10-10");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldExceptionWithIncorrectEmail() {
        User user = new User(0, "4dsadasdas", "login", "name", "2000-10-10");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldExceptionWithIncorrectBirthday() {
        User user = new User(0, "sasadas@dfsdfd.com", "login", "name", "2200-10-10");
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        User user = new User(1, "sasadas@dfsdfd.com", "loginUpdate", "name", "2000-10-10");
        assertThrows(ValidationException.class, () -> userController.update(user));
    }

    @Test
    void shouldNotExceptionWithEmptyName() {
        User user = new User(0, "sasadas@dfsdfd.com", "login", "", "2000-10-10");
        userController.create(user);
        assertEquals(user.getName(), user.getLogin());
    }
}