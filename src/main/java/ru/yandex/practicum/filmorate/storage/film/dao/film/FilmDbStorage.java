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
import ru.yandex.practicum.filmorate.errorException.exception.EntityNotFoundException;
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
        String updateQuery = "UPDATE Film SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE id=?";
        int filmId = film.getId();

        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                filmId
        });

        int[] updateResults = jdbcTemplate.batchUpdate(updateQuery, batchArgs);

        if (updateResults[0] > 0) {
            log.info("Film with ID {} has been updated.", filmId);
        } else {
            throw new EntityNotFoundException("Film not found for update by ID=" + filmId);
        }

        String deleteGenreQuery = "DELETE FROM Genre_Film WHERE film_id = ?";
        jdbcTemplate.update(deleteGenreQuery, filmId);

        String insertGenreQuery = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (?, ?)";
        List<Object[]> genreBatchArgs = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            genreBatchArgs.add(new Object[]{filmId, genre.getId()});
        }
        jdbcTemplate.batchUpdate(insertGenreQuery, genreBatchArgs);

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
