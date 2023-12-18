package ru.yandex.practicum.filmorate.errorException;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
