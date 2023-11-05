package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;

import java.time.LocalDate;


@Getter
@Setter
@NonFinal
public class Film extends AbstractModel {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        super(id, name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}