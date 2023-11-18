package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import storage.user.InMemoryUserStorage;
import storage.user.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService = new UserService();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @PostMapping
    public User addUser(@RequestBody User user) {
        checkBody(user);
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        checkBody(user);
        return inMemoryUserStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @DeleteMapping
    public User deleteUser(User user) {
        return inMemoryUserStorage.deleteUser(user.getId());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@RequestHeader Integer id, Integer userId) {
        userService.addFriend(id, userId);
        return inMemoryUserStorage.getUserId(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteUser(@RequestHeader Integer id, Integer friendId) {
        userService.deleteFriend(id, friendId);
        return inMemoryUserStorage.getUserId(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> listFriends(@RequestHeader Integer userId) {
        return userService.friends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@RequestHeader Integer id, Integer otherId) {
        return userService.mutualFriends(id, otherId);
    }

    private static void checkBody(User user) throws ValidationException {
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
