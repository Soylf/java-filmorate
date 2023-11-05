package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;

@Getter
@Setter
@NonFinal
public class AbstractModel {
    private Integer id;
    private String name;

    public AbstractModel(String name) {
        this.name = name;
    }
}
