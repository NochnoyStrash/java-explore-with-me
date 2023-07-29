package ru.practicum.error;

public class StatsValidateException extends  RuntimeException {
    public StatsValidateException(String massage) {
        super(massage);
    }
}
