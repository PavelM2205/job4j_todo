package ru.job4j.exception;

public class UserWithSuchLoginAlreadyExists extends RuntimeException {
    public UserWithSuchLoginAlreadyExists(String message) {
        super(message);
    }
}
