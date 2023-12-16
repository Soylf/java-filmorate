package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User  {

    private Integer id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends;



    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }

    public Set<Integer> getFriends() {
        return friends;
    }
}