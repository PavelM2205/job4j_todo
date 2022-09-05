package ru.job4j.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tasks")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-EEEE-yyyy HH:mm:ss");
    private boolean done;

    @Override
    public String toString() {
        return "Task{"
                + "id=" + id + ", description='" + description + '\''
                + ", created=" + FORMATTER.format(created)
                + ", done=" + done + '}';
    }
}
