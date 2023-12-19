package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.errorException.ValidationException;
import ru.yandex.practicum.filmorate.errorException.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@Slf4j
@Repository
@AllArgsConstructor
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }


    private Map<String, Object> userToMap(User user) {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("email", user.getEmail());
        userAttributes.put("login", user.getLogin());
        userAttributes.put("user_name", user.getName());
        userAttributes.put("birthday", user.getBirthday());
        return userAttributes;
    }


    @Override
    public List<User> getAllUsers() {
        String query = "SELECT user_id, email, login, user_name, birthday FROM User_Filmorate";
        log.debug("All users returned from DB");
        return jdbcTemplate.query(query, this::mapToUser);
    }

    @Override
    public User addUser(User user) {
        checkUser(user);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("User_Filmorate")
                .usingGeneratedKeyColumns("user_id");
        Number key = simpleJdbcInsert.executeAndReturnKey(userToMap(user));
        user.setId((Integer) key);
        log.debug("User with ID {} saved.", user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        String query = "SELECT user_id, email, login, user_name, birthday FROM User_Filmorate WHERE user_id=?";
        log.info("users returned from DB");
        return jdbcTemplate.queryForObject(query, this::mapToUser, id);
    }

    @Override
    public User updateUser(User user) {
        checkUser(user);

        String query = "UPDATE User_Filmorate SET email=?, login=?, user_name=?, birthday=? WHERE user_id=?";
        int userId = user.getId();
        int updateResult = jdbcTemplate.update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                userId);
        if (updateResult > 0) {
            log.debug("User with ID {} has been updated.", userId);
        } else {
            throw new EntityNotFoundException("User not founded for update by ID=" + userId);
        }
        return user;
    }

    @Override
    public void deleteUserById(Integer user_id) {
        String query = "DELETE FROM User_Filmorate WHERE user_id=?";
        int deleteResult = jdbcTemplate.update(query, user_id);
        if (deleteResult > 0) {
            log.info("User with ID {} has been removed.", user_id);
        } else {
            throw new EntityNotFoundException("User not found for deletion by ID=" + user_id);
        }
    }

    @Override
    public List<User> getFriendsByUserId(Integer id) {
        String query = "SELECT uf.user_id, uf.email, uf.login, uf.user_name, uf.birthday " +
                "FROM User_Filmorate uf " +
                "JOIN Friendship f ON uf.user_id = f.friend_id " +
                "WHERE f.user_id = ?";
        log.debug("All friends of user by ID {} returned from DB", id);
        return jdbcTemplate.query(query, this::mapToUser, id);
    }

    @Override
    public void deleteFriendById(Integer userId, Integer friendId) {
        String query = "DELETE FROM Friendship WHERE user_id = ? AND friend_id = ?";
        int deleteResult = jdbcTemplate.update(query, userId, friendId);
        if (deleteResult > 0) {
            log.info("User with ID {} has been removed from friends of user by ID {}.", userId, friendId);
        } else {
            throw new EntityNotFoundException("Users are not friends");
        }
    }

    @Override
    public void addFriend(Integer userId, Integer idFriend) {
        if (userId <= 0 || idFriend <= 0) {
            throw new EntityNotFoundException("Users with same id not exists");
        }
        String query = "INSERT INTO Friendship (user_id, friend_id) " +
                "SELECT ?, ? " +
                "WHERE NOT EXISTS ( " +
                "SELECT 1 FROM Friendship " +
                "WHERE user_id = ? AND friend_id = ?)";
        int insertResult = jdbcTemplate.update(query, userId, idFriend, userId, idFriend);
        if (insertResult > 0) {
            log.info("User with ID {} has been added in friends of user by ID {}.", idFriend, userId);
        }
    }

    @Override
    public List<User> mutualFriends(Integer sourceId, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        String query = "SELECT u.user_id, u.email, u.login, u.user_name, u.birthday FROM Friendship f1 " +
                "INNER JOIN Friendship f2 ON f1.friend_id = f2.friend_id " +
                "INNER JOIN User_Filmorate u ON f1.friend_id = u.user_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ? AND f1.friend_id = f2.friend_id";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, sourceId, otherId);
        while (sqlRowSet.next()) {
            int friendId = sqlRowSet.getInt("user_id");
            commonFriends.add(getUserById(friendId));
        }
        return commonFriends;
    }

    private static void checkUser(User user) throws ValidationException {
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
