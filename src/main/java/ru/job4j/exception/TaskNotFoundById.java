package ru.job4j.exception;

public class TaskNotFoundById extends RuntimeException {
    public TaskNotFoundById(String message) {
        super(message);
    }
}
