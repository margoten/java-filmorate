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
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();
}
