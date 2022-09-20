package com.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private String releaseDate;
    @NonNull
    private int duration;
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
}
