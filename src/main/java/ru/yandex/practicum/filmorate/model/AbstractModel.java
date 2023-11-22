package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AbstractModel {

    @NonFinal
    @Setter
    private Integer id;

    @NonFinal
    @NotBlank
    @Size(max = 50)
    private String name;

    public AbstractModel(Integer id, String name) {
        this.name = name;
        this.id = id;
    }
}
