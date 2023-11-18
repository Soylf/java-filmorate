package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
public class Film extends AbstractModel {

    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Map<Film, Set<Integer>> like;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        super(id, name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(Film film, Integer idUser) {
        like.put(film, Set.of(idUser));
    }

    public void removeLike(Film film, Integer idUser) {
        like.remove(film, Set.of(idUser));
    }


}