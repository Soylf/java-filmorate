package ru.yandex.practicum.filmorate.storage.user.service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.errorException.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;


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
        return userStorage.addUser(user);
    }

    public User getUserById(Integer id) {
        try {
            return userStorage.getUserById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User with id " + id + " does not exist.");
        }
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public boolean deleteUserById(Integer id) {
        userStorage.deleteUserById(id);
        return true;
    }

    public boolean addFriend(Integer userId, Integer idFriend) {
        userStorage.addFriend(userId, idFriend);
        return true;
    }

    public boolean deleteFriendById(Integer userId, Integer idFriend) {
        userStorage.deleteFriendById(userId, idFriend);
        return true;
    }

    public List<User> getFriendsByIdUser(Integer id) {
        return userStorage.getFriendsByUserId(id);
    }

    public List<User> mutualFriends(Integer userId, Integer idFriend) {
        return new ArrayList<>(userStorage.mutualFriends(userId, idFriend));
    }
}
