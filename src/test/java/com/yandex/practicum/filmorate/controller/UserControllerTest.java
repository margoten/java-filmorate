package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    @Autowired
    private UserController userController;
    private User template;

    @BeforeEach
    public void createUser() {
        template = new User(0, "sasadas@dfsdfd.com", "login", "name",
                "2000-10-10");
    }

    public User createFriend() {
        return new User(0, "friend@dfsdfd.com", "loginFriend", "nameFriend",
                "2001-10-10");
    }

    @Test
    void shouldExceptionWithNull() {
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(null));
        Assertions.assertEquals("Пользователь не может быть создан.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyLogin() {
        template.setLogin("");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(template));
        Assertions.assertEquals("Логин не может быть пустым.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectLogin() {
        template.setLogin("l o g i n");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(template));
        Assertions.assertEquals("Некоррекстный логин l o g i n.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithEmptyEmail() {
        template.setEmail("");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(template));
        Assertions.assertEquals("Некорретсный адрес электронной почты .", ex.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectEmail() {
        template.setEmail("4dsadasdas");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(template));
        Assertions.assertEquals("Некорретсный адрес электронной почты 4dsadasdas.", ex.getMessage());
    }

    @Test
    void shouldExceptionWithIncorrectBirthday() {
        template.setBirthday("2200-10-10");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(template));
        Assertions.assertEquals("Неверная дата рождения 2200-10-10.", ex.getMessage());
    }

    @Test
    void shouldExceptionUpdateWithNonContainsId() {
        template.setId(2);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.update(template));
        Assertions.assertEquals("Пользователь не существует.", ex.getMessage());
    }

    @Test
    void shouldNotExceptionWithEmptyName() {
        template.setName("");
        User user = userController.create(template);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void shouldCorrectUserReturn() {
        User user = userController.create(template);
        User returned = userController.getUser(user.getId());
        assertNotNull(returned);

    }

    @Test
    void shouldExceptionWithNotFoundUser() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.getUser(-1));
        Assertions.assertEquals("Пользователя с id = " + -1 + " не существует.", ex.getMessage());
    }

    @Test
    void shouldCorrectUserAddFriend() {
        User created = userController.create(template);
        User friend = userController.create(createFriend());
        userController.addToFriends(created.getId(), friend.getId());

        assertEquals(friend.getFriends().size(), 1);
        assertTrue(friend.getFriends().contains(created.getId()));
        assertTrue(created.getFriends().contains(friend.getId()));
    }

    @Test
    void shouldExceptionAddToFriendNotFoundUser() {
        User created = userController.create(template);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.addToFriends(created.getId(), -6));
        Assertions.assertEquals("Пользователя с id = " + -6 + " не существует.", ex.getMessage());
    }

    @Test
    void shouldCorrectUserRemoveFriend() {
        User created = userController.create(template);
        User friend = userController.create(createFriend());
        userController.addToFriends(created.getId(), friend.getId());
        userController.removeFromFriends(created.getId(), friend.getId());

        assertFalse(friend.getFriends().contains(created.getId()));
        assertFalse(created.getFriends().contains(friend.getId()));
        assertEquals(friend.getFriends().size(), 0);

    }

    @Test
    void shouldExceptionRemoveFriendNotFoundUser() {
        User created = userController.create(template);
        NotFoundException ex = assertThrows(NotFoundException.class, () -> userController.removeFromFriends(created.getId(), -6));
        Assertions.assertEquals("Пользователя с id = " + -6 + " не существует.", ex.getMessage());
    }

    @Test
    void shouldCorrectUserFriendReturn() {
        User created = userController.create(template);
        User friend = userController.create(createFriend());
        userController.addToFriends(created.getId(), friend.getId());

        assertTrue(created.getFriends().contains(friend.getId()));
        assertEquals(friend.getFriends().size(), 1);
    }

    @Test
    void shouldEmptyUserFriendReturn() {
        User created = userController.create(template);
        assertNotNull(created.getFriends());
        assertTrue(created.getFriends().isEmpty());
    }

    @Test
    void shouldCorrectUsersCommonFriendReturn() {
        User created = userController.create(template);
        User friend = userController.create(createFriend());
        User common = userController.create(createFriend());
        userController.addToFriends(created.getId(), friend.getId());
        userController.addToFriends(created.getId(), common.getId());
        userController.addToFriends(friend.getId(), common.getId());

        assertEquals(friend.getFriends().size(), 2);
        assertFalse(userController.getCommonUserFriends(created.getId(), friend.getId()).isEmpty());

        assertTrue(userController.getCommonUserFriends(created.getId(), friend.getId()).contains(common));
        assertTrue(userController.getCommonUserFriends(created.getId(), common.getId()).contains(friend));

    }

}