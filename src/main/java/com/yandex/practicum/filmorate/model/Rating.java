package com.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

//
//    G,
//    PG,
//    PG_13,
//    R,
//    NC

@Data
@AllArgsConstructor
public class Rating {
    private int id;
    private String name;
}

