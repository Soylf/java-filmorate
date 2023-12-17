package ru.yandex.practicum.filmorate.storage.film.dao.mpa;

import ru.yandex.practicum.filmorate.model.Components.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    public Optional<Mpa> getMpaById(Integer id);

    List<Mpa> getAllMpa();
}
