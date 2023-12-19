package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.Mpa;


import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film extends AbstractModel {


    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer rate;
    private List<Genre> genres = new ArrayList<>();

    private Mpa mpa;


}