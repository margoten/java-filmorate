package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Film;
import com.yandex.practicum.filmorate.model.Mpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface MpaStorage {
    List<Mpa> getMpa();

    Optional<Mpa> get(int id);

    int getFilmMpa(int filmId);

    void setFilmMpa(int filmId, int mpaId);
}
