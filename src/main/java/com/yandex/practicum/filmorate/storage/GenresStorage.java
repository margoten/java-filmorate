package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public interface GenresStorage {
    List<Genre> getGenres();

    Optional<Genre> get(int id);
    Map<Integer, Genre> getGenres(List<Integer> ids);
    void addFilmGenre(int filmId, int genreId);

    List<Genre> getFilmGenres(int filmId);
}
