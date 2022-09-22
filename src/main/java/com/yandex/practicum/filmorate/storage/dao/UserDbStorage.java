package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.UserStorage;
import com.yandex.practicum.filmorate.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return jdbcTemplate.query(select, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> get(int id) {
        String select = "SELECT * FROM users WHERE id = ?";
        User user =  jdbcTemplate.queryForObject(select, (rs, rowNum) -> makeUser(rs), id);
        if(user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        String insert = "INSERT INTO users (id, email, login, name, birthday) VALUES ( ?, ?, ?, ?,?)";
        jdbcTemplate.update(insert, user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        String delete = "DELETE FROM users WHERE id = ?";
        String insert = "INSERT INTO users (id, email, login, name, birthday) VALUES ( ?, ?, ?, ?,?)";

        jdbcTemplate.update(delete, user.getId());
        jdbcTemplate.update(insert, user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return null;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        String birthday = rs.getDate("birthday").toLocalDate().format(Util.DATE_FORMAT);

        return new User(id, email, login, name, birthday);
    }
}
