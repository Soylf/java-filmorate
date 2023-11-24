package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUserId(Integer id);

    User addUser(User user);

    User deleteUser(Integer id);

    User updateUser(User user);

    List<User> getAllUsers();

}
