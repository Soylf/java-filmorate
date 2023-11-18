package storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User addUser(User user);

    public User deleteUser(Integer id);

    public User updateUser(User user);

    public List<User> getAllUsers();
}
