package ru.practicum;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String massage) {
        super(massage);
    }
}
