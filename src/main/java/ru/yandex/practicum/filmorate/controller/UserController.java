package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Ui.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 0;

    @PostMapping
    public User addUser(@RequestBody User user) {
        checkBody(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        checkBody(user);

        User userCurl = users.get(user.getId());
        if (Objects.nonNull(userCurl)) {
            userCurl.setName(user.getName());
            userCurl.setLogin(user.getLogin());
            userCurl.setBirthday(user.getBirthday());
            userCurl.setEmail(user.getEmail());

            users.put(user.getId(), userCurl);
            return userCurl;
        }
        throw new ValidationException("Пользователь не найден.");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
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

    private Integer generateId() {
        return idGenerator++;
    }
}