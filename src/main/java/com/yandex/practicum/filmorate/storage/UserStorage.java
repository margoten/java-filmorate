package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getUsers();

    Optional<User> get(int id);

    User create(User user);

    User update(User user);
}
