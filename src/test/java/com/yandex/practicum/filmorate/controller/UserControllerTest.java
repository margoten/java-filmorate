package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private static UserController userController;
    private User user;

    @BeforeAll
    public static void createController() {
        userController = new UserController();
    }

    @BeforeEach
    public void createFilm() {
        user = new User(0, "sasadas@dfsdfd.com", "login", "name", "2000-10-10");
    }

    @Test
    void shouldExceptionWithNull() {
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(null));
        Assertions.assertEquals("Пользователь не может быть создан.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyLogin() {
        user.setLogin("");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Логин не может быть пустым.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectLogin() {
        user.setLogin("l o g i n");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Некоррекстный логин l o g i n.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyEmail() {
        user.setEmail("");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Некорретсный адрес электронной почты .", ex.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectEmail() {
        user.setEmail("4dsadasdas");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Некорретсный адрес электронной почты 4dsadasdas.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectBirthday() {
        user.setBirthday("2200-10-10");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals("Неверная дата рождения 2200-10-10.", ex.getMessage());
    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        user.setId(2);
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.update(user));
        Assertions.assertEquals("Пользователь не существует.", ex.getMessage());
    }

    @Test
    void shouldNotExceptionWithEmptyName() {
        user.setName("");
        userController.create(user);
        assertEquals(user.getName(), user.getLogin());
    }
}