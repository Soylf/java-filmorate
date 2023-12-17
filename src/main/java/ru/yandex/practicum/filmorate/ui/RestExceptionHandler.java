package ru.yandex.practicum.filmorate.ui;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.ui.exception.ApiError;
import ru.yandex.practicum.filmorate.ui.exception.BadRequestException;
import ru.yandex.practicum.filmorate.ui.exception.EntityNotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleRuntimeEx(EntityNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError("Объект не найден", ex.getMessage());
        logger.debug(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestExceptionEx(BadRequestException ex, WebRequest request) {
        ApiError error = new ApiError("Неверный запрос", ex.getMessage());
        logger.debug(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Throwable.class})
    protected Object handleThrowable(Throwable ex, WebRequest request) {
        ApiError error = new ApiError("Внутренняя ошибка сервера", ex.getMessage());
        logger.debug(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders header, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        logger.debug("Данные не прошли валидацию.");
        ApiError apiError = new ApiError("Некорректные данные", ex.getMessage(), errors);
        return new ResponseEntity<>(apiError, status);
    }

}
