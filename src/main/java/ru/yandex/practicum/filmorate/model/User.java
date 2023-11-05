package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.NonFinal;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NonFinal
@NotBlank
public class User {
    private Integer id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
}