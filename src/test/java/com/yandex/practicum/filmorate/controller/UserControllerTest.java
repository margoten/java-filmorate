package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.service.UserService;
import com.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private static UserController userController;
    private User user;

    @BeforeAll
    public static void createController() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @BeforeEach
    public void createUser() {
        user = new User(0, "sasadas@dfsdfd.com", "login", "name", "2000-10-10", null);
    }

    public User createFriend() {
        return new User(0, "friend@dfsdfd.com", "loginFriend", "nameFriend", "2001-10-10", null);
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
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.update(user));
        Assertions.assertEquals("Пользователь не существует.", ex.getMessage());
    }

    @Test
    void shouldNotExceptionWithEmptyName() {
        user.setName("");
        userController.create(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldCorrectUserReturn() {
        userController.create(user);
        User returned = userController.getUser(1);
        assertNotNull(returned);

    }

    @Test
    void shouldExceptionWithNotFoundUser() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.getUser(1));
        Assertions.assertEquals("Пользователя с id = " + 1 + " не существует.", ex.getMessage());
    }

    @Test
    void shouldCorrectUserAddFriend() {
        userController.create(user);
        User friend = createFriend();
        userController.create(friend);
        userController.addToFriends(1, 2);

        assertEquals(friend.getFriends().size(), 1);
        assertTrue(friend.getFriends().contains(1));
        assertTrue(user.getFriends().contains(2));

    }

    @Test
    void shouldExceptionAddToFriendNotFoundUser() {
        userController.create(user);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.addToFriends(1, -6));
        Assertions.assertEquals("Пользователя с id = " + 3 + " не существует.", ex.getMessage());
    }

}