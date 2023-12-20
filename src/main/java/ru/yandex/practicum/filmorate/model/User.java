package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractModel {

    private String email;
    private String login;
    private LocalDate birthday;
}