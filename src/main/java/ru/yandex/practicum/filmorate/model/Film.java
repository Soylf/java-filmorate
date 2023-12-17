package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Components.Genre;
import ru.yandex.practicum.filmorate.model.Components.Mpa;


import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
public class Film extends AbstractModel {


    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    @JsonIgnore
    private Set<Integer> like = new HashSet<>();

    private List<Genre> genres = new ArrayList<>();

    private Mpa mpa;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa, List<Genre> genres) {
        super(id, name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }


    public Film() {
        super();
    }
}