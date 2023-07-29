package ru.practicum.events.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String massage) {
        super(massage);
    }
}
