package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.*;

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
        assertThrows(IllegalArgumentException.class, () -> taskService.findById(0));
    }
}