package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.IdGenerator;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    List<Film> films = new ArrayList<>();
    IdGenerator idGenerator = new IdGenerator();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        checkBody(film);
        film.setId(idGenerator.generateId());
        films.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        checkBody(film);
        Film film1 = films.get(film.getId() - 1);
        if (Objects.nonNull(film1)) {
            film1.setName(film.getName());
            film1.setDuration(film.getDuration());
            film1.setDescription(film.getDescription());
            film1.setReleaseDate(film.getReleaseDate());

            films.set(film.getId() - 1, film1);
            return film1;
        }
        throw new ValidationException("Фильм не найден.");
    }


    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }


    private static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    private static void checkBody(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
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
}