package ru.yandex.practicum.filmorate.model.сomponents;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Mpa {
    private Integer id;
    private String name;

    public Mpa(Integer id) {
        this.id = id;
    }
}
