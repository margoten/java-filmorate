package com.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


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
}
