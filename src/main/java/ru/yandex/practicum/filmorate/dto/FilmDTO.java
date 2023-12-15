package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Components.Genre;



import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class FilmDTO {
    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;
    private MpaDTO mpaDTO;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
}