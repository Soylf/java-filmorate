package ru.yandex.practicum.filmorate.storage.film.dao.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.errorException.ValidationException;
import ru.yandex.practicum.filmorate.errorException.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.genre.GenreStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }

    private RowMapper<Film> mapToFilm() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));

            Mpa mpa = new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
            film.setMpa(mpa);

            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
            return film;
        };
    }

    @Override
    public Film addFilm(Film film) {
        checkFilm(film);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("Film").usingGeneratedKeyColumns("id");
        Number key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        film.setId((Integer) key);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String query = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }
        log.debug("Film with ID {} saved.", film.getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        String query = "DELETE FROM Film WHERE id = ?";
        int deleteResult = jdbcTemplate.update(query, id);
        if (deleteResult > 0) {
            log.info("Film with ID {} has been removed.", id);
        } else {
            throw new EntityNotFoundException("Film not found for deletion by ID=" + id);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilm(film);

        int filmId = film.getId();
        String query = "UPDATE Film SET film_name=?, description=?, release_date=?, duration=?, rate =?, mpa_id=? " + "WHERE id=?";
        int updateResult = jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), filmId);
        if (updateResult > 0) {
            log.debug("Film with ID {} has been updated.", filmId);
        } else {
            throw new EntityNotFoundException("Film not founded for update by ID=" + filmId);
        }
        if (!film.getGenres().isEmpty()) {
            String querySql = "DELETE FROM Genre_Film WHERE film_id =?";
            jdbcTemplate.update(querySql, filmId);
            String insertGenreQuery = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (?, ?)";
            film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertGenreQuery, filmId, genre.getId());
            }
        } else {
            String querySql = "DELETE FROM Genre_Film WHERE film_id =?";
            jdbcTemplate.update(querySql, filmId);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "SELECT f.id, f.film_name, f.description, f.release_date, f.duration, f.mpa_id, m.name " + "FROM Film f " + "JOIN Mpa m ON f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(query, mapToFilm());
    }

    @Override
    public Film getFilmId(Integer id) {
        try {
            String query = "SELECT f.id, f.film_name, f.description, f.release_date, f.duration,f.mpa_id, m.name " + "FROM Film f " + "JOIN Mpa m ON f.mpa_id = m.mpa_id " + "WHERE f.id = ?";
            return jdbcTemplate.queryForObject(query, mapToFilm(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Film not exist");
        }
    }

    @Override
    public List<Film> popularFilm(Integer countFilms) {
        String query = "SELECT f.id, f.film_name, f.description, f.release_date, f.duration, f.rate, " + "f.mpa_id, m.name, COUNT(lf.user_id) AS likes " + "FROM Film f " + "LEFT JOIN Like_Film lf ON f.id = lf.film_id " + "JOIN Mpa m ON f.mpa_id = m.mpa_id " + "GROUP BY f.id " + "ORDER BY likes DESC " + "LIMIT ?";

        RowMapper<Film> filmRowMapper = mapToFilm();
        return jdbcTemplate.query(query, filmRowMapper, countFilms);
    }

    private static void checkFilm(Film film) throws ValidationException {
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