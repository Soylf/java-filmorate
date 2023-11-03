package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    List<User> users = new ArrayList<>();
    IdGenerator idGenerator = new IdGenerator();



    @PostMapping
    public void addUser(@RequestBody User user) {
        checkBody(user);
            user.setId(idGenerator.generateId());
            users.add(user);

    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) {
        checkBody(user);
            users.set(id,user);

    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }


    private static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    private void checkBody(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный адрес электронной почты");
        }else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        }else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
