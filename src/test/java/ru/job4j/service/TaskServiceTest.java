package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.exception.TaskNotFoundById;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AllArgsConstructor
public class TaskServiceTest {
    private static SessionFactory sf;

    @BeforeAll
    public static void getSf() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }


    @Test
    public void whenTaskNotExistWithSpecifyIdThenFindByIdThrowsException() {
        TaskRepository repository = new TaskRepository(sf);
        TaskService taskService = new TaskService(repository);
        assertThrows(TaskNotFoundById.class, () -> taskService.findById(0));
    }

    @Test
    public void whenAddTaskReturnsOptionalEmptyThenMustBeException() {
        TaskRepository taskStore = mock(TaskRepository.class);
        TaskService service = new TaskService(taskStore);
        Task task = mock(Task.class);
        when(taskStore.add(task)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> service.add(task));
    }
}