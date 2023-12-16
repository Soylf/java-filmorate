package ru.yandex.practicum.filmorate.storage.film.dao.film;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Components.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.mpa.MpaStorage;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Qualifier("filmDbStorage")
@AllArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;


    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa());
        return values;
    }


    private static RowMapper<Film> getFilmMapper() {
        return (rs, rowNum) -> new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration")
        );
    }


    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Film")
                .usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        film.setId((Integer) key);

        if (!film.getGenres().isEmpty()) {
            String query = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
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
