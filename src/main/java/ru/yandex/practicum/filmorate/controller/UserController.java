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

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("POST /user");
        if (checkBody(user)) {
            user.setId(idGenerator.generateId());
            users.set(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Бонг, чёт не так???");
        }

    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        log.info("PUT /user/" + id);
        if (checkBody(user)) {
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
        } else {
            throw new ValidationException("Бонг, чёт не так???");
        }

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

    private boolean checkBody(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Некорректный адрес электронной почты");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Некорректный логин");
        } else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
        } else if (user != users.get(user.getId())) {
            log.error("Такого пользователя нету");
        }
        return true;
    }
}