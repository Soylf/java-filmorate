package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDTO {
    private int id;

    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

    private List<User> friends;
}