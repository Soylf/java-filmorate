package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@Getter
@Setter
@NonFinal
public class User extends AbstractModel {

    private String email;
    private String login;
    private LocalDate birthday;


    public User(String email, String login, String name, LocalDate birthday) {
        super(name);
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}