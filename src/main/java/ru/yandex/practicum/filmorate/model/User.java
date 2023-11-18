package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class User extends AbstractModel {

    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        super(id, name);
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }

    public List<Integer> getFriends() {
        return new ArrayList<>(friends);
    }
}