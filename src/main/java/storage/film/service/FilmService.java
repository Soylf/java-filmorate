package storage.film.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import storage.film.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    Map<Film, Set<Integer>> liked;

    public void addLike(int idFilm, int idUser) {
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        film.addLike(film, idUser);
        liked.put(film, Set.of(idUser));
    }

    public void deleteLike(int idFilm, int idUser) {
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        film.removeLike(film, idUser);
        liked.remove(film, Set.of(idUser));
    }

    public List<Film> popularFilm(Integer count) {
        int numbsFilm = 10;

        if (!(count == null)) {
            numbsFilm = count;
        }

        Map<Film, Set<Integer>> sortedLike = liked.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(numbsFilm)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(sortedLike.keySet());
    }
}
