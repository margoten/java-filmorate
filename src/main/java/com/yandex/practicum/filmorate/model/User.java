package com.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private String birthday;
    private Set<Integer> friends;

    public Set<Integer> getFriends() {
        if(friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }
}
