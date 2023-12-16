package ru.yandex.practicum.filmorate.storage.user.service;


import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    protected final UserStorage userDbStorage;


    public List<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public User addUser(User user) {
        checkBody(user);
        return userDbStorage.addUser(user);
    }

    public User getUserById(int id) {
        try {
            return userDbStorage.getUserById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User with id " + id + " does not exist.");
        }
    }

    public User updateUser(User user) {
        checkBody(user);
        return userDbStorage.updateUser(user);
    }

    public boolean deleteUserById(int id) {
        userDbStorage.deleteUserById(id);
        return true;
    }

    public boolean addFriend(int userId, int idFriend) {
        userDbStorage.addFriend(userId, idFriend);
        return true;
    }

    public boolean deleteFriendById(int userId, int idFriend) {
        userDbStorage.deleteFriendById(userId, idFriend);
        return true;
    }

    public Set<Integer> getFriendsByIdUser(int id) {
        return userDbStorage.getFriendsByUserId(id);
    }

    public List<User> mutualFriends(int userId, int idFriend) {
        return new ArrayList<>(userDbStorage.mutualFriends(userId, idFriend));
    }

    private static void checkBody(User user) throws ru.yandex.practicum.filmorate.ui.ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ru.yandex.practicum.filmorate.ui.ValidationException("Некорректный адрес электронной почты");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ru.yandex.practicum.filmorate.ui.ValidationException("Некорректный логин");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ru.yandex.practicum.filmorate.ui.ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
