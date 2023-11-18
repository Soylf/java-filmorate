package storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film addFilm(Film film);

    public void deleteFilm(Integer id);

    public Film updateFilm(Film film);

    public List<Film> getAllFilms();
}
