package ru.practicum.events.exceptions;

public class EventConflictException extends RuntimeException {
    public EventConflictException(String massage) {
        super(massage);
    }
}
