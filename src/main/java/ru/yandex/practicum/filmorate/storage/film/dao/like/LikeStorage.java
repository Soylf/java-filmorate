package ru.yandex.practicum.filmorate.storage.film.dao.like;

import java.util.Set;

public interface LikeStorage {
    void addLike(Integer idFilm, Integer idUser);

    void removeLike(Integer idFilm, Integer idUser);

    Set<Integer> getFilmLikesById(Integer idFilm);
}
