package ru.yandex.practicum.filmorate.storage.film.dao.film;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.film.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;


import java.util.*;

@Repository
@Qualifier("filmDbStorage")
@AllArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;



    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa());
        return values;
    }


    private RowMapper<Film> getFilmMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setMpa(mpaStorage.getMpaById(rs.getInt("mpa_id")).get());
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
            return film;
        };
    }


    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Film")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> filmMap = filmToMap(film);
        Number key = simpleJdbcInsert.executeAndReturnKey(filmMap);
        film.setId((Integer) key);

        List<Map<String, Object>> genreFilmMaps = new ArrayList<>();
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                Map<String, Object> genreFilmMap = new HashMap<>();
                genreFilmMap.put("film_id", film.getId());
                genreFilmMap.put("genre_id", genre.getId());
                genreFilmMaps.add(genreFilmMap);
            }
        }

        if (!genreFilmMaps.isEmpty()) {
            String query = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (:film_id, :genre_id)";
            SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(genreFilmMaps.toArray());
            jdbcTemplate.batchUpdate(query, Collections.singletonList(batchParams));
        }

        log.info("Film with ID {} saved.", film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        String query = "DELETE FROM Film WHERE id=?";
        int deleteResult = jdbcTemplate.update(query, id);
        if (deleteResult > 0) {
            log.info("Film with ID {} has been removed.", id);
        } else {
            log.info("Film with ID {} has not been deleted.", id);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String query = "UPDATE Film SET name=?, description=?, release_date=?, duration=?, mpa_id=? " +
                "WHERE id=?";
        int filmId = film.getId();
        int updateResult = jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()), // Преобразование LocalDate в java.sql.Date
                film.getDuration(),
                film.getMpa().getId(),
                filmId);
        if (updateResult > 0) {
            log.info("Film with ID {} has been updated.", filmId);
        } else {
            throw new EntityNotFoundException("Film not found for update by ID=" + filmId);
        }

        String deleteGenreQuery = "DELETE FROM Genre_Film WHERE film_id = ?";
        jdbcTemplate.update(deleteGenreQuery, filmId);

        String insertGenreQuery = "INSERT INTO genre_film (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(insertGenreQuery, filmId, genre.getId());
        }

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "SELECT * FROM Film";
        log.info("All films returned from DB");
        return jdbcTemplate.query(query, getFilmMapper());
    }

    @Override
    public Film getFilmId(Integer id) {
        String query = "SELECT * FROM Film WHERE id = ?";
        log.info("Film ID {} returned from DB", id);
        return jdbcTemplate.queryForObject(query, getFilmMapper(), id);
    }
    @Override
    public List<Film> popularFilm(Integer countFilms) {
        String query = "SELECT f.*, COUNT(lf.id) AS likes " +
                "FROM Film f " +
                "LEFT JOIN Like_film lf ON f.id = lf.film_id " +
                "GROUP BY f.id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(query, getFilmMapper(), countFilms);
    }
}
