package ru.yandex.practicum.filmorate.storage.user.service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.errorException.ValidationException;
import ru.yandex.practicum.filmorate.errorException.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class UserService {
    protected final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        checkUser(user);
        return userStorage.addUser(user);
    }

    public User getUserById(int id) {
        try {
            return userStorage.getUserById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User with id " + id + " does not exist.");
        }
    }

    public User updateUser(User user) {
        checkUser(user);
        return userStorage.updateUser(user);
    }

    public boolean deleteUserById(int id) {
        userStorage.deleteUserById(id);
        return true;
    }

    public boolean addFriend(int userId, int idFriend) {
        userStorage.addFriend(userId, idFriend);
        return true;
    }

    public boolean deleteFriendById(int userId, int idFriend) {
        userStorage.deleteFriendById(userId, idFriend);
        return true;
    }

    public Set<Integer> getFriendsByIdUser(int id) {
        return userStorage.getFriendsByUserId(id);
    }

    public List<User> mutualFriends(int userId, int idFriend) {
        return new ArrayList<>(userStorage.mutualFriends(userId, idFriend));
    }

    private static void checkUser(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный адрес электронной почты");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
