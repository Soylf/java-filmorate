package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NonFinal
public class User {
    private Integer id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
}