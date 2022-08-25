package com.yandex.practicum.filmorate.model.comparator;

import com.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmsComparator implements Comparator<Film> {

    @Override
    public int compare(Film o1, Film o2) {
        return o2.getLikes().size() - o1.getLikes().size();
    }
}
