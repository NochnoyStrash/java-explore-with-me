package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.categories.exception.CategoryNotFoundException;
import ru.practicum.compilations.exception.CompilationNotFoundException;
import ru.practicum.events.exceptions.*;
import ru.practicum.requests.exception.RequestNotFoundException;
import ru.practicum.requests.exception.RequestValidationException;
import ru.practicum.user.exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({UserNotFoundException.class, EventNotFoundException.class, CommentNotFoundException.class,
            RequestNotFoundException.class, CategoryNotFoundException.class, CompilationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerObjectNotFound(RuntimeException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.toString(), "Сушность не существует либо указан неправильный ID");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerObjectConflict(EventValidateException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), "Данные не прошли валидацию");
    }

    @ExceptionHandler({EventConflictException.class, RequestValidationException.class, ValidateCommentException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerConflict(RuntimeException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.toString(), "Данные не прошли валидацию");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExceptions(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), "ошибка запроса");
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectUserData(SQLException e) {
        log.warn(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage(), HttpStatus.CREATED.toString(), "Запрос имеет конфликты");
    }


}
