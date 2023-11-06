package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Ui.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;


    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        checkBody(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        checkBody(film);

        Film filmCurl = films.get(film.getId());
        if (Objects.nonNull(filmCurl)) {
            filmCurl.setName(film.getName());
            filmCurl.setDuration(film.getDuration());
            filmCurl.setDescription(film.getDescription());
            filmCurl.setReleaseDate(film.getReleaseDate());

            films.put(film.getId(), filmCurl);
            return filmCurl;
        }
        throw new ValidationException("Фильм не найден.");
    }


    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }


    private static void checkBody(Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName().contains(" ")) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

    }


    private Integer generateId() {
        return idGenerator++;
    }
}