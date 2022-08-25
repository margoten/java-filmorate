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
    private Set<Integer> likes;

    public Set<Integer> getLikes() {
        if(likes == null) {
            likes = new HashSet<>();
        }
        return likes;
    }



}
