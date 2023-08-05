package ru.practicum.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.events.exceptions.EventValidateException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<StackTraceElement> errors;
    private final String massage;
    private final String status;
    private final String reason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ErrorResponse(String massage, String status, String reason) {
        this.errors = new ArrayList<>();
        this.massage = massage;
        this.status = status;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerObjectConflict(EventValidateException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), "Данные не прошли валидацию");
    }
}
