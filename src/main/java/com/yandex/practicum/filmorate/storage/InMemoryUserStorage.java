package com.yandex.practicum.filmorate.storage;

import com.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.of(users.get(id));
    }

    @Override
    public Optional<User> update(User user) {
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public void addToFriend(User targetUser, User friend) {
        targetUser.getFriends().add(friend.getId());
    }

    @Override
    public void removeFromFriend(User targetUser, User friend) {
        targetUser.getFriends().remove(friend.getId());
    }

    @Override
    public void deleteUser(int id) {

    }
}
