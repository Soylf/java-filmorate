package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.Ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;


@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idGenerator = 1;

    private void generateId() {
        idGenerator++;
    }


    @Override
    public Film addFilm(Film film) {
        checkBody(film);

        film.setId(idGenerator);
        films.put(idGenerator, film);
        generateId();

        return film;
    }

    @Override
    public Film deleteFilm(Integer id) {
        if (films.containsKey(id)) {
            throw new ValidationException("Такого фильма нету.");
        }
        return films.remove(id);
    }

    @Override
    public Film updateFilm(Film film) {
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

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmId(Integer id) {
        if (!(films.containsKey(id))) {
            throw new EntityNotFoundException("Пользователь с id= " + id + " не найден.");
        }
        return films.get(id);
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
