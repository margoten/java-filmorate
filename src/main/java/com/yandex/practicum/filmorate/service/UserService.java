package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.UserStorage;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j

public class UserService {

    private final UserStorage userStorage;
    private int idGenerator = 0;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не может быть создан.");
        }
        validationUser(user);

        user.setId(generatedId());
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не может быть обновлен.");
        }
        if (userStorage.get(user.getId()).isEmpty()) {
            log.warn("Пользователь с id {} не существует.", user.getId());
            throw new NotFoundException("Пользователь не существует.");
        }
        validationUser(user);
        return userStorage.update(user);
    }

    public User getUser(Integer userId) {
        Optional<User> user = userStorage.get(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
        return user.get();
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addToFriends(int targetUserId, int friendId) {
        User targetUser = getUser(targetUserId);
        User friend = getUser(friendId);
        targetUser.getFriends().add(friendId);
        friend.getFriends().add(targetUserId);
    }

    public void removeFromFriends(int targetUserId, int friendId) {
        User targetUser = getUser(targetUserId);
        User friend = getUser(friendId);
        targetUser.getFriends().remove(friendId);
        friend.getFriends().remove(targetUserId);
    }

    public List<User> getFriends(int userId) {
        User user = getUser(userId);
        return user.getFriends().stream()
                .map(userStorage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int targetUserId, int otherUserId) {
        User targetUser = getUser(targetUserId);
        User otherUser = getUser(otherUserId);
        return targetUser.getFriends().stream().filter(id -> otherUser.getFriends().contains(id))
                .map(userStorage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private void validationUser(User user) {
        if (user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым.");
            throw new ValidationException("Логин не может быть пустым.");
        }

        if (user.getLogin().contains(" ")) {
            log.warn("Некорректный логин {}.", user.getLogin());
            throw new ValidationException("Некорректный логин " + user.getLogin() + ".");

        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Некорректный адрес электронной почты {}.", user.getEmail());
            throw new ValidationException("Некорректный адрес электронной почты " + user.getEmail() + ".");

        }

        if (LocalDate.parse(user.getBirthday(), Util.DATE_FORMAT).isAfter(LocalDate.now())) {
            log.warn("Неверная дата рождения {}.", user.getBirthday());
            throw new ValidationException("Неверная дата рождения " + user.getBirthday() + ".");

        }

        if (user.getName().isBlank()) {
            log.warn("Имя пользователя пустое. Используем для имения значение логина {}.", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private int generatedId() {
        return ++idGenerator;
    }
}
