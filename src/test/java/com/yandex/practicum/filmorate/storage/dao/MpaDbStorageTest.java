package com.yandex.practicum.filmorate.storage.dao;

import com.yandex.practicum.filmorate.controller.FilmController;
import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.storage.dao.GenresDbStorage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void testGetMpa() {
        List<Mpa> mpaList = mpaDbStorage.getMpa();

        assertFalse(mpaList.isEmpty());
        assertEquals(5, mpaList.size());
        assertEquals(mpaList.get(0).getName(), "G");
    }

    @Test
    void testGetMpaById() {
        Optional<Mpa> mpaOptional = mpaDbStorage.getMpaById(3);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13"));
    }
}