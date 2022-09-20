package ru.job4j.exception;

public class UserWithSuchLoginAndPasswordDoesNotExists extends RuntimeException {
    public UserWithSuchLoginAndPasswordDoesNotExists(String message) {
        super(message);
    }
}
