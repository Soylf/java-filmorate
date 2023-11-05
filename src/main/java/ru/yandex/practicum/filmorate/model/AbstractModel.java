package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractModel {
    private Integer id;
    private String name;

    public AbstractModel(Integer id, String name) {
        this.name = name;
        this.id = id;
    }
}
