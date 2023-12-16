package ru.yandex.practicum.filmorate.storage.film.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.dao.like.LikeStorage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.Ui.exception.*;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }


    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        checkBody(film);
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(int id) {
        try {
            return filmStorage.getFilmId(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Film with id " + id + " does not exist.");
        }
    }

    public Film updateFilm(Film film) {
        checkBody(film);
        return filmStorage.updateFilm(film);
    }

    public void deleteFilmById(int id) {
        filmStorage.deleteFilm(id);
    }

    public boolean addLike(int idFilm, int idUser) {
        likeStorage.addLike(idFilm, idUser);
        return true;
    }

    public boolean deleteLike(int idFilm, int idUser) {
        likeStorage.removeLike(idFilm, idUser);
        return true;
    }

    public List<Film> popularFilm(Integer count) {
        return filmStorage.popularFilm(count);
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
