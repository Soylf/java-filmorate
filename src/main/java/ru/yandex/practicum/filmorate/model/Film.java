package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film extends AbstractModel {

    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    @JsonIgnore
    private Set<Integer> like = new HashSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        super(id, name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}