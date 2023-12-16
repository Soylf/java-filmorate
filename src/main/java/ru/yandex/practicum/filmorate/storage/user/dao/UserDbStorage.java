package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@Repository
@Qualifier("userDbStorage")
@AllArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        int userId = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        Set<Integer> friends =  getFriendsByUserId(userId);

        return User.builder()
                .id(userId)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .friends(friends)
                .build();
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("email", user.getEmail());
        userAttributes.put("login", user.getLogin());
        userAttributes.put("name", user.getName());
        userAttributes.put("birthday", user.getBirthday());
        return userAttributes;
    }


    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM user";
        log.info("All users returned from DB");
        return jdbcTemplate.query(query, this::mapToUser);
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(userToMap(user));
        user.setId((Integer) key);
        log.info("User with ID {} saved.", user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        String query = "SELECT * FROM user WHERE id=";
        log.info("users returned from DB");
        return jdbcTemplate.queryForObject(query, this::mapToUser, id);
    }

    @Override
    public User updateUser(User user) {
        String query = "UPDATE user SET email=?, login=?, name=?, birthday=? WHERE id=?";
        int userId = user.getId();
        int updateResult = jdbcTemplate.update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                userId);
        if (updateResult > 0) {
            log.info("User with ID {} has been updated.", userId);
        } else {
            throw new EntityNotFoundException("User not founded for update by ID=" + userId);
        }
        return user;
    }

    @Override
    public void deleteUserById(Integer id) {
        String query = "DELETE FROM user WHERE id=?";
        int deleteResult = jdbcTemplate.update(query, id);
        if (deleteResult > 0) {
            log.info("User with ID {} has been removed.", id);
        } else {
            log.info("User with ID {} has not been deleted.", id);
        }
    }

    @Override
    public Set<Integer> getFriendsByUserId(Integer id) {
        String query = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<Integer> friendIds = jdbcTemplate.queryForList(query, Integer.class, id);
        return new HashSet<>(friendIds);
    }

    @Override
    public void deleteFriendById(Integer userId, Integer idFriend) {
        String query = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
        int deleteResult = jdbcTemplate.update(query, userId, idFriend);
        if (deleteResult > 0) {
            log.info("User with ID {} has been removed from friends of user by ID {}.", userId, idFriend);
        } else {
            log.info("Users are not friends");
        }
    }

    @Override
    public void addFriend(Integer userId, Integer idFriend) {
        if (userId <= 0 || idFriend <= 0) {
            throw new EntityNotFoundException("Users with same id not exists");
        }
        String query = "INSERT INTO friends (user_id, friend_id) " +
                "SELECT ?, ? " +
                "WHERE NOT EXISTS ( " +
                "SELECT 1 FROM friends " +
                "WHERE user_id = ? AND friend_id = ?)";
        int insertResult = jdbcTemplate.update(query, userId, idFriend, userId, idFriend);
        if (insertResult > 0) {
            log.info("User with ID {} has been added in friends of user by ID {}.", idFriend, userId);
        }
    }

    @Override
    public List<User> mutualFriends(Integer sourceId, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        String query = "SELECT u.* FROM friends f1 " +
                "INNER JOIN friends f2 ON f1.friend_id = f2.friend_id " +
                "INNER JOIN user u ON f1.friend_id = u.id " +
                "WHERE f1.user_id = ? AND f2.user_id = ? AND f1.friend_id = f2.friend_id";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, sourceId, otherId);
        while (sqlRowSet.next()) {
            int friendId = sqlRowSet.getInt("user_id");
            commonFriends.add(getUserById(friendId));
        }
        return commonFriends;
    }

}
