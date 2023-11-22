package ru.yandex.practicum.filmorate.storage.user.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public User addFriend(Integer id, Integer friendId) {
        User sourceUser = userStorage.getUserId(id);
        User targetUser = userStorage.getUserId(friendId);

        sourceUser.addFriend(friendId);
        targetUser.addFriend(id);
        log.info("Пользователь в друзьях");
        return userStorage.getUserId(id);
    }

    public boolean deleteFriend(Integer id, Integer friendId) {
        User sourceUser = userStorage.getUserId(id);
        User targetUser = userStorage.getUserId(friendId);

        sourceUser.deleteFriend(targetUser.getId());
        targetUser.deleteFriend(sourceUser.getId());
        log.info("Пользователь удален из друзей");
        return true;
    }

    public List<User> mutualFriends(Integer sourceId, Integer otherId) {
        User sourceUser = userStorage.getUserId(sourceId);
        User targetUser = userStorage.getUserId(otherId);

        if (sourceUser.getFriends().isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> userId = sourceUser.getFriends().stream().filter(identity -> targetUser.getFriends().contains(identity)).collect(Collectors.toList());

        return userStorage.getAllUsers().stream().filter(user -> userId.contains(user.getId())).collect(Collectors.toList());

    }

    public List<User> getFriends(Integer userId) {
        User user = userStorage.getUserId(userId);

        if (user.getFriends() == null) {
            throw new EntityNotFoundException("Друзей нет >:3");
        }
        Set<Integer> friendId = user.getFriends();
        List<User> users = new ArrayList<>();
        friendId.forEach(numbs -> users.add(userStorage.getUserId(numbs)));

        return users;
    }
}
