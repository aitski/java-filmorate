package ru.yandex.practicum.filmorate.exceptions;

public class Validation extends RuntimeException {
    public Validation(String message) {
        super(message);
    }
}