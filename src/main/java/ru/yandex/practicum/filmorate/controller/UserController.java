package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.IdGenerator2;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    List<User> users = new ArrayList<>();
    IdGenerator2 idGenerator = new IdGenerator2();

    @PutMapping
    public User addUser(@RequestBody User user) {
        log.info("POST /user");
        checkBody(user);
        user.setId(idGenerator.generateId());
        users.set(user.getId(), user);
        return user;

    }

    @PostMapping
    public User updateUser(@RequestBody User user) {
        log.info("PUT /user/" + user.getId());
        checkBody(user);
        User user1 = users.get(user.getId());
        if (Objects.nonNull(user1)) {
            user1.setName(user.getName());
            user1.setLogin(user.getLogin());
            user1.setBirthday(user.getBirthday());
            user1.setEmail(user.getEmail());

            users.set(user.getId(), user1);
            return user1;
        }
        throw new ValidationException("Пользователь не найден.");
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("GET /user");
        return new ArrayList<>(users);
    }


    private static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    private static void checkBody(@RequestBody User user) throws ValidationException {
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