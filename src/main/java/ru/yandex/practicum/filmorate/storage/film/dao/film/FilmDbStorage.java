package ru.yandex.practicum.filmorate.storage.film.dao.film;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Components.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage, LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likeStorage = likeStorage;
    }


    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", film.getName());
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
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        Number key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        film.setId((Integer) key);

        if (!film.getGenres().isEmpty()) {
            String query = "INSERT INTO genre_film (film_id, genre_id) VALUES (?,?)";
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
        String query = "UPDATE film SET name=?, description=?, release_date=?, duration=?, mpa_id=? " +
                "WHERE id=?";
        int filmId = film.getId();
        int updateResult = jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                filmId);
        if (updateResult > 0) {
            log.info("Film with ID {} has been updated.", filmId);
        } else {
            throw new EntityNotFoundException("Film not founded for update by ID=" + filmId);
        }

        if (!film.getGenres().isEmpty()) {
            String querySql = "DELETE FROM genre_film WHERE id =?";
            jdbcTemplate.update(querySql, filmId);
            String insertGenreQuery = "INSERT INTO genre_film (id, genre_id) VALUES (?, ?)";
            film.setGenres(film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList()));
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertGenreQuery, filmId, genre.getId());
            }
        } else {
            String querySql = "DELETE FROM genre_film WHERE id =?";
            jdbcTemplate.update(querySql, filmId);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "SELECT * FROM film";
        log.info("All films returned from DB");
        return jdbcTemplate.query(query, getFilmMapper());
    }

    @Override
    public Film getFilmId(Integer id) {
        String query = "SELECT * FROM film WHERE id = ";
        log.info("films id returned from DB");
        return jdbcTemplate.queryForObject(query,getFilmMapper(),id);
    }
    @Override
    public List<Film> popularFilm(Integer countFilms) {
        String query = "SELECT f.*, COUNT(lf.id) AS likes " +
                "FROM Film f " +
                "LEFT JOIN Like_Film lf ON f.film_id = lf.id " +
                "GROUP BY f.id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(query, getFilmMapper(), countFilms);
    }
}
