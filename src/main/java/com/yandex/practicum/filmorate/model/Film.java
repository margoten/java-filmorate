package com.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@Builder
public class Film {
    private int id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    private int duration;
    private Integer mpaId;
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
}
