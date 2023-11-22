package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;


import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.service.UserService;

import java.util.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("addUser_запушен");
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(value = "id") Integer id) {
        return userStorage.getUserId(id);
    }

    @DeleteMapping
    public User deleteUser(User user) {
        return userStorage.deleteUser(user.getId());
    }


    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(value = "id") Integer id, @PathVariable(value = "friendId") Integer userId) {
        log.info("запущен_addFriend");
        return userService.addFriend(id, userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable(value = "id") Integer id, @PathVariable(value = "id") Integer friendId) {
        log.info("запущен_deleteFriend");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> listFriends(@PathVariable(value = "id") Integer userId) {
        log.info("запущен_ListFriend");
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable(value = "id") Integer id, @PathVariable(value = "otherId") Integer otherId) {
        log.info("запущен_mutualFriend");
        return userService.mutualFriends(id, otherId);
    }

}
