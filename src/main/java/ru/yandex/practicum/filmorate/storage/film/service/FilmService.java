package ru.yandex.practicum.filmorate.storage.film.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private static final Comparator<Film> COMPARATOR_LIKES = (curFilm, nextFilm) -> nextFilm.getLike().size() - curFilm.getLike().size();
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    public boolean addLike(Integer idFilm, Integer idUser) {
        Film film = filmStorage.getFilmId(idFilm);
        userStorage.getUserId(idUser);

        film.getLike().add(idUser);
        log.info(film.getName() + " на фильме " + film.getLike().size() + " лайков");
        return true;
    }

    public boolean deleteLike(Integer idFilm, Integer idUser) {
        Film film = filmStorage.getFilmId(idFilm);


        if (film.getLike().isEmpty()) {
            log.error("Ошибка при удалении лайку у фильма");
            throw new EntityNotFoundException("У данного фильма нет лайков");
        }

        film.getLike().remove(idUser);
        log.info(film.getName() + " на фильме " + film.getLike().size() + " лайков");
        return true;
    }

    public List<Film> popularFilm(Integer count) {
        List<Film> films = filmStorage.getAllFilms();
        films.sort(COMPARATOR_LIKES);

        log.info("выведен топ фильмов");
        return films.stream().limit(count).collect(Collectors.toList());
    }
}
