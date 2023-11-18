package storage.film;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;


@Component
public class InMemoryFilmStorage implements FilmStorage {
    Map<Integer, Film> films = new HashMap<>();
    private Integer idGenerator = 1;

    private Integer generateId() {
        return ++idGenerator;
    }

    @Override
    public Film addFilm(@RequestBody Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        films.remove(id);
        throw new ValidationException("Такого фильма нету.");
    }

    @Override
    public Film updateFilm(@RequestBody Film film) {

        Film filmCurl = films.get(film.getId());
        if (Objects.nonNull(filmCurl)) {
            filmCurl.setName(film.getName());
            filmCurl.setDuration(film.getDuration());
            filmCurl.setDescription(film.getDescription());
            filmCurl.setReleaseDate(film.getReleaseDate());

            films.put(film.getId(), filmCurl);
            return filmCurl;
        }
        throw new ValidationException("Фильм не найден.");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film getFilm(Integer id) {
        return films.get(id);
    }
}
