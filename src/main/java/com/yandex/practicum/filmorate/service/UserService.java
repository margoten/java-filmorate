package com.yandex.practicum.filmorate.service;

import com.yandex.practicum.filmorate.exeption.NotFoundException;
import com.yandex.practicum.filmorate.model.User;
import com.yandex.practicum.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public User getUser(Integer userId) {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " не существует.");
        }
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addToFriends(int targetUserId, int friendId) {
        User targetUser = getUser(targetUserId);
        User friend = getUser(friendId);
        targetUser.getFriends().add(friendId);
        friend.getFriends().add(targetUserId);
    }

    public void removeFromFriends(int targetUserId, int friendId) {
        User targetUser = getUser(targetUserId);
        User friend = getUser(friendId);
        targetUser.getFriends().remove(friendId);
        friend.getFriends().remove(targetUserId);
    }

    public List<User> getFriends(int userId) {
        User user = getUser(userId);
        return user.getFriends().stream()
                .map(userStorage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int targetUserId, int otherUserId) {
        User targetUser = getUser(targetUserId);
        User otherUser = getUser(otherUserId);
        return targetUser.getFriends().stream().filter(id -> otherUser.getFriends().contains(id))
                .map(userStorage::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
