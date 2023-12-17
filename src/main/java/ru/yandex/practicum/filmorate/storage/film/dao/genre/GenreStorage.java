package ru.yandex.practicum.filmorate.storage.film.dao.genre;

import ru.yandex.practicum.filmorate.model.сomponents.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
        Optional<Genre> getById(Integer id);

        List<Genre> getAll();

        List<Genre> getGenresByFilmId(Integer filmId);
}
