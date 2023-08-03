package ru.practicum.events.exceptions;

public class ValidateCommentException extends  RuntimeException {
    public ValidateCommentException(String massage) {
        super(massage);
    }
}
