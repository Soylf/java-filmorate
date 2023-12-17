package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.model.Components.AbstractModel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class User extends AbstractModel {

    private Integer id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        super(id, name);
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}