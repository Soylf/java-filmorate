package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.Ui.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void addFilmWithNullName() {
        Film film = new Film(0, " ", "gff", LocalDate.now(), 150);
        Film film1 = new Film(0, "", "gff", LocalDate.now(), 150);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
    }

    @Test
    void addFilmWithDuration201() {
        String desc = "21";
        Film film = new Film(0, "gf", desc.repeat(500), LocalDate.now(), 200);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void addFilmWithReleaseDate() {
        LocalDate localDate = LocalDate.of(1895, 12, 27);
        Film film = new Film(0, "hgg", "gff", localDate, 201);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void addFilmWithMovieDuration() {
        Film film = new Film(0, "hgg", "gff", LocalDate.now(), -201);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }
}