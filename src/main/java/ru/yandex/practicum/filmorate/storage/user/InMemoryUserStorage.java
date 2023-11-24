package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ui.ValidationException;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 1;

    private void generateId() {
        idGenerator++;
    }


    @Override
    public User addUser(User user) {
        checkBody(user);

        user.setId(idGenerator);
        users.put(idGenerator, user);
        generateId();

        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        if (users.containsKey(id)) {
            throw new ValidationException("Такого пользователя нету.");
        }
        return users.remove(id);
    }

    @Override
    public User updateUser(User user) {
        checkBody(user);

        User userCurl = users.get(user.getId());
        if (Objects.nonNull(userCurl)) {
            userCurl.setName(user.getName());
            userCurl.setLogin(user.getLogin());
            userCurl.setBirthday(user.getBirthday());
            userCurl.setEmail(user.getEmail());

            users.put(userCurl.getId(), userCurl);
            return userCurl;
        }
        throw new ValidationException("Пользователь не найден.");
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserId(Integer id) {
        if (!(users.containsKey(id))) {
            throw new EntityNotFoundException("Пользователь с id= " + id + " не найден.");
        }
        return users.get(id);
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