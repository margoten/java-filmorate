package com.yandex.practicum.filmorate.controller;

import com.yandex.practicum.filmorate.model.Genre;
import com.yandex.practicum.filmorate.model.Mpa;
import com.yandex.practicum.filmorate.service.GenresService;
import com.yandex.practicum.filmorate.service.MpaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreController {
    private final GenresService genresService;

    @GetMapping()
    public List<Genre> getAll() {
        return genresService.getAllGenres();
    }


    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        return genresService.getGenre(id);
    }
}