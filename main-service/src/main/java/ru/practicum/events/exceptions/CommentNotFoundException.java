package ru.practicum.events.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String massage) {
        super(massage);
    }
}
