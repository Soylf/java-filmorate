package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;


import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.service.FilmService;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;


    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("addFilm_запушен");
        return filmStorage.addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmId(@PathVariable(value = "id") Integer id) {
        log.info("getFilmId_запушен");
        return filmStorage.getFilmId(id);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("updateFilm_запушен");
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable(value = "id") Integer id) {
        log.info("deleteFilm_запушен");
        return filmStorage.deleteFilm(id);
    }


    @GetMapping
    public List<Film> getAllFilms() {
        log.info("getAllFilms_запушен");
        return filmStorage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable(value = "id") Integer id, @PathVariable(value = "userId") Integer userId) {
        log.info("addLike_запушен");
        return filmService.addLike(id, userId);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable(value = "id") Integer id, @PathVariable(value = "userId") Integer userId) {
        log.info("deleteLike_запушен");
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> popularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("popularFilms_запушен");
        return filmService.popularFilm(count);
    }


}