package ru.job4j.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private int id;
    private String description;
    private LocalDateTime created;
    private boolean done;
}
