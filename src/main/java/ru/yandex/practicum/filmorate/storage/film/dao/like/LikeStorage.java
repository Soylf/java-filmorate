package ru.yandex.practicum.filmorate.storage.film.dao.like;

import java.util.Set;

public interface LikeStorage {
    void addLike(int idFilm, int idUser);

    void removeLike(int idFilm, int idUser);

    Set<Integer> getFilmLikesById(int idFilm);
}
