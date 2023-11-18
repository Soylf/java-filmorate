package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import storage.film.InMemoryFilmStorage;
import storage.film.service.FilmService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService();


    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        checkBody(film);
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        checkBody(film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    @DeleteMapping
    public Film deleteFilm(Integer id) {
        inMemoryFilmStorage.deleteFilm(id);
        return inMemoryFilmStorage.getFilm(id);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@RequestHeader int id, int userId) {
        filmService.addLike(id, userId);
        return inMemoryFilmStorage.getFilm(id);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@RequestHeader int id, int userId) {
        filmService.deleteLike(id, userId);
        return inMemoryFilmStorage.getFilm(id);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> popularFilms(@RequestHeader int count) {
        return filmService.popularFilm(count);
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