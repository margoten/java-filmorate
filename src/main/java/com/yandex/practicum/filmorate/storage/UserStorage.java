package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User get(int id);

    User create(User user);

    User update(User user);
}
