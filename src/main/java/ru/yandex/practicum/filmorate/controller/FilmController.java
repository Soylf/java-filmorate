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
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.debug("POST /film");
        if (checkBody(film)) {
            film.setId(idGenerator.generateId());
            films.add(film);
            return film;
        } else {
            throw new ValidationException("Бонг, чёт не так???");
        }

    }

    @PutMapping("/{id}")
    public Film updateFilm(@PathVariable int id, @RequestBody Film film) throws ValidationException {
        log.info("PUT /films/" + id);
        checkBody(film);
        if (checkBody(film)) {
            Film film1 = films.get(film.getId());
            if (Objects.nonNull(film1)) {
                film1.setName(film.getName());
                film1.setDuration(film.getDuration());
                film1.setDescription(film.getDescription());
                film1.setReleaseDate(film.getReleaseDate());

                films.set(film.getId(), film1);
                return film1;
            }
            throw new ValidationException("Фильм не найден.");
        } else {
            throw new ValidationException("Бонг, чёт не так???");
        }
    }


    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("GET /film");
        return new ArrayList<>(films);
    }


    private static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    private boolean checkBody(@RequestBody Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.error("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Описание фильма не может превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
        } else if (film != films.get(film.getId())) {
            log.error("Такого фильма нету");
        }
        return true;
    }
}