package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.exception.TaskNotFoundById;
import ru.job4j.model.Task;
import ru.job4j.model.User;
import ru.job4j.repository.TaskRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private static final String DEFAULT_TIME_ZONE = "UTC";
    private final TaskRepository taskRepository;

    public Task add(Task task) {
        Optional<Task> optTask = taskRepository.add(task);
        if (optTask.isEmpty()) {
            throw new IllegalStateException("Task was not added");
        }
        return optTask.get();
    }

    public Task findById(int id, User user) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundById("Task not found");
        }
        Task task = optionalTask.get();
        setTimeZoneByUser(task, user);
        return task;
    }

    public List<Task> findAll(User user) {
        List<Task> result = taskRepository.findAll();
        setTimeZoneByUser(result, user);
        return result;
    }

    public List<Task> findAllDone(User user) {
        List<Task> result = taskRepository.findAllDone();
        setTimeZoneByUser(result, user);
        return result;
    }

    public List<Task> findAllUndone(User user) {
        List<Task> result = taskRepository.findAllUndone();
        setTimeZoneByUser(result, user);
        return result;
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

    private void setTimeZoneByUser(Task task, User user) {
        String timeZone = user.getTimeZone() == null ? DEFAULT_TIME_ZONE
                : user.getTimeZone();
        ZonedDateTime zonedDateTime = task.getCreated()
                .atZone(ZoneId.systemDefault());
        ZonedDateTime userZonedDateTime = zonedDateTime.withZoneSameInstant(
                ZoneId.of(timeZone));
        task.setCreated(userZonedDateTime.toLocalDateTime());
    }

    private void setTimeZoneByUser(List<Task> tasks, User user) {
        tasks.forEach(task -> setTimeZoneByUser(task, user));
    }
}
