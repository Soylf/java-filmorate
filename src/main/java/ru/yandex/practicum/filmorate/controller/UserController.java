package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;


import ru.yandex.practicum.filmorate.storage.user.service.UserService;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("addUser_запушен");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("updateUser_запушен");
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("getAalUsers_запушен");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(value = "id") Integer id) {
        log.info("getUser_запушен");
        return userService.getUserById(id);
    }

    @DeleteMapping
    public boolean deleteUser(User user) {
        log.info("deleteUser_запушен");
        return userService.deleteUserById(user.getId());
    }


    @PutMapping("/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable(value = "id") Integer id, @PathVariable(value = "friendId") Integer userId) {
        log.info("запущен_addFriend");
        return userService.addFriend(id, userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable(value = "id") Integer id, @PathVariable(value = "id") Integer friendId) {
        log.info("запущен_deleteFriend");
        return userService.deleteFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<Integer> listFriends(@PathVariable(value = "id") Integer userId) {
        log.info("запущен_ListFriend");
        return userService.getFriendsByIdUser(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable(value = "id") Integer id, @PathVariable(value = "otherId") Integer otherId) {
        log.info("запущен_mutualFriend");
        return userService.mutualFriends(id, otherId);
    }

}
