package ru.yandex.practicum.filmorate.storage.film.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Components.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.mpa.MpaStorage;


import java.util.List;


@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(int id) {
        try {
            return mpaStorage.getMpaById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Mpa not exist by id=" + id));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Mpa not exist by id=" + id);
        }
    }


    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
