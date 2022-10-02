package ru.job4j.exception;

public class NoSpecifyCategory extends RuntimeException {
    public NoSpecifyCategory(String message) {
        super(message);
    }
}
