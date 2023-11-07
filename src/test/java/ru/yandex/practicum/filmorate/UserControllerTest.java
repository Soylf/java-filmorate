package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

public class UserControllerTest {
    UserController userController = new UserController();


    @Test
    void addUserWithEmptyLogins() throws ValidationException {
        User user = new User(1, "email@mail.com", "pp", "user", LocalDate.now());
        user.setName(" ");
        userController.addUser(user);
        assertEquals("pp", user.getLogin());
    }

    @Test
    void addUserWithFutureBirthDate() {
        User user = new User(1, "email@mail.com", "password", "user", LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void addUserWithEmptyGmail() {
        User user = new User(1, "email2mail.com", "pp", "user", LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void addUserWithNullLogin() {
        User user = new User(1, "email2mail.com", " ", "user", LocalDate.now());
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}
