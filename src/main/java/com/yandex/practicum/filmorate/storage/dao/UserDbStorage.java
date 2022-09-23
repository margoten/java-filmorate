package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.UserStorage;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        String select = "SELECT * FROM users";
        return jdbcTemplate.query(select, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> get(int id) {
        String select = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(select, id);
        if (userRow.next()) {
            User user = User.builder()
                    .id(userRow.getInt("id"))
                    .email(userRow.getString("email"))
                    .login(userRow.getString("login"))
                    .name(userRow.getString("name"))
                    .birthday(userRow.getDate("birthday").toLocalDate())
                    .build();
            user.getFriends().addAll(getUserFriends(id));

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        String insert = "INSERT INTO users (ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insert, user.getId(), user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
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

    private List<Integer> getUserFriends(int userId) {
        String select = "SELECT friends_id \n" +
                "FROM user_friends \n" +
                "WHERE user_id = ?";
        return jdbcTemplate.query(select, (rs, rowNum) -> rs.getInt("friends_id"), userId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        User user = User.builder().id(id)
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        user.getFriends().addAll(getUserFriends(id));
        return user;
    }
}
