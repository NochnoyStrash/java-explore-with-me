package ru.practicum.events.exceptions;

public class EventValidateException extends RuntimeException {
    public EventValidateException(String massage) {
        super(massage);
    }
}
