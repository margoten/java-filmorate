package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String select = "SELECT * FROM users";
        return null;
    }

    @Override
    public Optional<User> get(int id) {
        String select = "SELECT * FROM users WHERE id = ?";
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        String insert = "INSERT INTO user (id, email, login, name, birthday) VALUES ( ?, ?, ?, ?,?)";
        return null;
    }

    @Override
    public User update(User user) {
        String delete = "DELETE FROM user WHERE id = ?";
        String insert = "INSERT INTO user (id, email, login, name, birthday) VALUES ( ?, ?, ?, ?,?)";
        return null;
    }
}
