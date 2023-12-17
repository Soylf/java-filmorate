package ru.yandex.practicum.filmorate.storage.film.dao.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;

import java.util.HashSet;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor
@Qualifier("LikeDbStorage")
public class LikeDbStorage implements  LikeStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int idFilm, int idUser) {
        String query = "INSERT INTO Like_Film (film_id, user_id) " +
                "SELECT ?, ? " +
                "WHERE NOT EXISTS (" +
                "SELECT 1 FROM Like_Film " +
                "WHERE film_id = ? AND user_id = ?)";
        int insertResult = jdbcTemplate.update(query, idFilm, idUser, idFilm, idUser);
        if (insertResult > 0) {
            log.info("User with ID {} has added like for film by ID {}.", idUser, idFilm);
        }
    }

    @Override
    public void removeLike(int idFilm, int idUser) {
        if (idUser < 1) {
            throw new EntityNotFoundException("User not exists by ID=" + idUser);
        }
        String query = "DELETE FROM Like_Film WHERE film_id=? AND user_id=?";
        int insertResult = jdbcTemplate.update(query, idFilm, idUser);
        if (insertResult > 0) {
            log.info("User with ID {} has removed like for film by ID {}.", idUser, idFilm);
        }
    }

    @Override
    public Set<Integer> getFilmLikesById(int idFilm) {
        String query = "SELECT user_id FROM Like_Film WHERE film_id=?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, idFilm);
        Set<Integer> likedUsers = new HashSet<>();
        while (sqlRowSet.next()) {
            likedUsers.add(sqlRowSet.getInt("user_id"));
        }
        return likedUsers;
    }
}
