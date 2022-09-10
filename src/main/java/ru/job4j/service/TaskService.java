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

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
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

    public boolean delete(int id) {
        return taskRepository.delete(id);
    }
}
