package ru.yandex.practicum.filmorate.model.Components;

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
