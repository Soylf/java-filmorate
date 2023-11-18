package storage.user;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 1;

    private Integer generateId() {
        return ++idGenerator;
    }

    @Override
    public User addUser(@RequestBody User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Integer id) {
        users.remove(id);
        throw new ValidationException("Такого пользователя нету.");
    }

    @Override
    public User updateUser(@RequestBody User user) {

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

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserId(Integer id) {
        return users.get(id);
    }

}
