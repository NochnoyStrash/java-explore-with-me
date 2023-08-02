package ru.practicum.categories.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String massage) {
        super(massage);
    }
}
