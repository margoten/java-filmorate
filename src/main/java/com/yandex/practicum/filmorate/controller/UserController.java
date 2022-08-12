package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.exeption.ValidationException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    @GetMapping()
    public Collection<User> getAll() {
        log.debug("Текущее количество фильмов: {}", users.size());
        return users.values();
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        if (user == null) {
            throw new ValidationException("Пользователь не может быть создан.");
        }
        if (!validUser(user)) {
            throw new ValidationException("Пользователь не может быть создан.");
        }
        user.setId(idGenerator.incrementAndGet());
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
        if (!validUser(user)) {
            throw new ValidationException("Пользователь не может быть обновлен.");
        }

        users.put(user.getId(), user);
        return user;
    }

    private boolean validUser(User user) {
        if (user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым.");
            return false;
        }

        if (user.getLogin().contains(" ")) {
            log.warn("Некоррекстный логин {}.", user.getLogin());
            return false;
        }

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Некорретсный адрес электронной почты {}.", user.getEmail());
            return false;
        }

        if (LocalDate.parse(user.getBirthday(), Util.DATE_FORMAT).isAfter(LocalDate.now())) {
            log.warn("Неверная дата рождения {}.", user.getBirthday());
            return false;
        }

        if (user.getName().isBlank()) {
            log.warn("Имя пользователя пустое. Используем для имения значение логина {}.", user.getLogin());
            user.setName(user.getLogin());
        }
        return true;
    }
}
