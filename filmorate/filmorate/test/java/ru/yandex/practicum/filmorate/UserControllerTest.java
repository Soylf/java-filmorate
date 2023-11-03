package ru.yandex.practicum.filmorate;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCheckBodyWhenEmailIsEmptyThenThrowValidationException() throws Exception {
        User user = new User(1,"email@example.com","g","gg",LocalDate.now());
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenEmailDoesNotContainAtSymbolThenThrowValidationException() throws Exception {
        User user = new User(1,"email", "login","gg", LocalDate.now());
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenLoginIsEmptyThenThrowValidationException() throws Exception {
        User user = new User(1,"email@example.com", "","gg", LocalDate.now());
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenLoginContainsSpacesThenThrowValidationException() throws Exception {
        User user = new User(1,"email@example.com", "login with spaces","2" ,LocalDate.now());
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckBodyWhenDisplayNameIsEmptyThenUseLoginAsDisplayName() throws Exception {
        User user = new User(1,"email@example.com", "login","f" ,LocalDate.now());
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckBodyWhenDisplayNameIsNotEmptyThenUseDisplayName() throws Exception {
        User user = new User(1,"email@example.com", "login", "2",LocalDate.now());
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckBodyWhenBirthdayIsInFutureThenThrowValidationException() throws Exception {
        User user = new User(1,"email@example.com", "login","2" ,LocalDate.now().plusDays(1));
        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}