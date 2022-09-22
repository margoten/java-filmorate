package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.UserStorage;
import com.yandex.practicum.filmorate.utils.Util;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(select, id);
        if(filmRow.next()) {
            User user = new User(
                    filmRow.getInt("id"),
                    filmRow.getString("email"),
                    filmRow.getString("login"),
                    filmRow.getString("name"),
                    filmRow.getDate("birthday").toLocalDate().format(Util.DATE_FORMAT));

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        LocalDate localDate = LocalDate.parse(user.getBirthday(), Util.DATE_FORMAT);

        String insert = "INSERT INTO users (ID, EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insert, user.getId(), user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(localDate));
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
