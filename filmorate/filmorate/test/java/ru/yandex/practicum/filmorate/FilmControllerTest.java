package ru.yandex.practicum.filmorate;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmController filmController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCheckBodyWhenFilmNameIsEmptyThenThrowValidationException() throws Exception {
        Film film = new Film(1,"ab","dec",LocalDate.now(),120);
        mockMvc.perform(post("/film")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenFilmDescriptionIsLongerThan200CharactersThenThrowValidationException() throws Exception {
        String longDescription = new String(new char[201]).replace("\0", "a");
        Film film = new Film(1,"ab",longDescription,LocalDate.now(), 120);
        mockMvc.perform(post("/film")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenFilmReleaseDateIsBefore28thDecember1895ThenThrowValidationException() throws Exception {
        Film film = new Film(1,"dec","description", LocalDate.of(1895, 12, 27), 120);
        mockMvc.perform(post("/film")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenFilmDurationIsZeroThenThrowValidationException() throws Exception {
        Film film = new Film(1,"ab","dec",LocalDate.now(), 0);
        mockMvc.perform(post("/film")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenFilmIsValidThenNoExceptionThrown() throws Exception {
        Film film = new Film(1,"ab","dec",LocalDate.now(), 120);
        mockMvc.perform(post("/film")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}