package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.IdGenerator;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    List<User> users = new ArrayList<>();
    IdGenerator idGenerator = new IdGenerator();



    @PostMapping
    public void addUser(@RequestBody User user) {
        if(checkBody(user).equals("1")){
            user.setId(idGenerator.generateId());
            users.add(user);
        }else {
            checkBody(user);
        }
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) {
        if(checkBody(user).equals("1")){
            users.set(id,user);
        }else {
            checkBody(user);
        }
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

    private String checkBody(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный адрес электронной почты");
        }else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        }else if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }return "1";
    }
}
