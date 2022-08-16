package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 0;

    @GetMapping()
    public List<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не может быть создан.");
        }
        validationUser(user);

        user.setId(generatedId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не может быть обновлен.");
        }
        if (!users.containsKey(user.getId())) {
            log.warn("Пользовател с id {} не существует.", user.getId());
            throw new ValidationException("Пользователь не существует.");
        }
        validationUser(user);

        users.put(user.getId(), user);
        return user;
    }

    private void validationUser(User user) {
        if (user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым.");
            throw new ValidationException("Логин не может быть пустым.");
        }

        if (user.getLogin().contains(" ")) {
            log.warn("Некоррекстный логин {}.", user.getLogin());
            throw new ValidationException("Некоррекстный логин " + user.getLogin() + ".");

        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Некорретсный адрес электронной почты {}.", user.getEmail());
            throw new ValidationException("Некорретсный адрес электронной почты " + user.getEmail() + ".");

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
