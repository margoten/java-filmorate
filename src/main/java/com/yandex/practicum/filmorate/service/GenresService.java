package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.storage.GenresStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenresService {
    private final GenresStorage genresStorage;

    public List<Genre> getAllGenres() {

        return genresStorage.getGenres();
    }

    public Genre getGenre(int id) {
        Optional<Genre> genre = genresStorage.get(id);
        if (genre.isEmpty()) {
            throw new NotFoundException("Жанр с id = " + id + " не существует.");
        }
        return genre.get();
    }

}
