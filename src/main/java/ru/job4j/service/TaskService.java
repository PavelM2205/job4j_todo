package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task add(Task task) {
        return taskRepository.add(task);
    }

    public Task findById(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new IllegalArgumentException("Task with that id already exists");
        }
        return optionalTask.get();
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllDone() {
        return taskRepository.findAllDone();
    }

    public List<Task> findAllUndone() {
        return taskRepository.findAllUndone();
    }

    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    public boolean setDone(int id) {
        return taskRepository.setDone(id);
    }

    public boolean delete(int id) {
        return taskRepository.delete(id);
    }
}
