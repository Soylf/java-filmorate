package ru.yandex.practicum.filmorate.storage.film.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Components.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.genre.GenreStorage;


import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
        private final GenreStorage genreStorage;

        public Genre getGenreById(int id) {
            try {
                return genreStorage.getById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Genre not exist by id=" + id));
            } catch (EmptyResultDataAccessException e) {
                throw new EntityNotFoundException("Genre not exist by id=" + id);
            }

        }

        public List<Genre> getAll() {
            return genreStorage.getAll();
        }

        public List<Genre> getGenresByFilmId(int filmId) {
            return genreStorage.getGenresByFilmId(filmId);
        }
    }
