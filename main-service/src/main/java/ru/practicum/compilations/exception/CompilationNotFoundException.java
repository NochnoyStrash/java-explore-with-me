package ru.practicum.compilations.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(String massage) {
        super(massage);
    }
}
