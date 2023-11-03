package ru.yandex.practicum.filmorate.controller;


import ru.yandex.practicum.filmorate.IdGenerator;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/film")
public class FilmController {
    List<Film> films = new ArrayList<>();
    IdGenerator idGenerator = new IdGenerator();


    @PostMapping
    public void addFilm(@RequestBody Film film) throws ValidationException {
            checkBody(film);
            film.setId(idGenerator.generateId());
            films.add(film);

    }

    @PutMapping("/{id}")
    public void updateFilm(@PathVariable int id, @RequestBody Film film) throws ValidationException {
            checkBody(film);
            films.set(id,film);

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

    private void checkBody(@RequestBody Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
