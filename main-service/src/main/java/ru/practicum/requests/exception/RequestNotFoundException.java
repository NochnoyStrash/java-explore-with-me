package ru.practicum.requests.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String massage) {
        super(massage);
    }
}
