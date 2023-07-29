package ru.practicum.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
}
