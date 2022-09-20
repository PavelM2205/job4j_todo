package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.exception.TaskNotFoundById;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task add(Task task) {
        Optional<Task> optTask = taskRepository.add(task);
        if (optTask.isEmpty()) {
            throw new IllegalStateException("Task was not added");
        }
        return optTask.get();
    }

    public Task findById(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundById("Task not found");
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

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void setDone(int id) {
        taskRepository.setDone(id);
    }

    public void delete(int id) {
        taskRepository.delete(id);
    }
}
