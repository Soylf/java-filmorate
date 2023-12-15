package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAllUsers();

    User addUser(User user);

    User getUserById(Integer id);

    User updateUser(User user);

    void deleteUserById(Integer id);

    Set<Integer> getFriendsByUserId(Integer id);

    void deleteFriendById(Integer userId, Integer idFriend);

    void addFriend(Integer userId, Integer idFriend);

    List<User> mutualFriends(Integer sourceId, Integer otherId);
}