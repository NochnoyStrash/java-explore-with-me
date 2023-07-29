package ru.practicum.requests.exception;

public class RequestValidationException extends RuntimeException {
    public RequestValidationException(String massage) {
        super(massage);
    }
}
