package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.model.Task;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

public class TaskRepositoryTest {
    private static StandardServiceRegistry registry;
    private static SessionFactory sf;

    @BeforeAll
    public static void getSf() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry).buildMetadata()
                .buildSessionFactory();
    }

    @AfterEach
    public void cleanTable() {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE Task").executeUpdate();
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    public void whenAdd() {
        TaskRepository repo = new TaskRepository(sf);
        Task task = new Task();
        task.setDescription("Text");
        task.setDone(true);
        repo.add(task);
        Task taskFromDB = repo.findById(task.getId()).get();
        assertThat(task.getId()).isNotEqualTo(0);
        assertThat(taskFromDB.getId()).isEqualTo(task.getId());
        assertThat(taskFromDB.getCreated()).isEqualTo(task.getCreated());
        assertThat(taskFromDB.isDone()).isEqualTo(task.isDone());
        assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void whenAddTwoTasksAndFindAllThenReturnsBoth() {
        TaskRepository repo = new TaskRepository(sf);
        Task task1 = new Task();
        task1.setDescription("task #1");
        task1.setDone(true);
        Task task2 = new Task();
        task2.setDescription("task #2");
        task2.setDone(false);
        repo.add(task1);
        repo.add(task2);
        List<Task> tasksFromDB = repo.findAll();
        assertThat(tasksFromDB.size()).isEqualTo(2);
        assertThat(tasksFromDB.get(0).getId()).isEqualTo(task1.getId());
        assertThat(tasksFromDB.get(0).getDescription())
                .isEqualTo(task1.getDescription());
        assertThat(tasksFromDB.get(0).getCreated()).isEqualTo(task1.getCreated());
        assertThat(tasksFromDB.get(0).isDone()).isEqualTo(task1.isDone());
        assertThat(tasksFromDB.get(1).getId()).isEqualTo(task2.getId());
        assertThat(tasksFromDB.get(1).getDescription())
                .isEqualTo(task2.getDescription());
        assertThat(tasksFromDB.get(1).getCreated()).isEqualTo(task2.getCreated());
        assertThat(tasksFromDB.get(1).isDone()).isEqualTo(task2.isDone());
    }

    @Test
    public void whenUpdateThenMustBeChangedTask() {
        TaskRepository repo = new TaskRepository(sf);
        Task task = new Task();
        task.setDescription("Task");
        task.setDone(false);
        repo.add(task);
        Task changedTask = new Task();
        changedTask.setDescription("Changed Task");
        changedTask.setDone(true);
        changedTask.setId(task.getId());
        boolean update = repo.update(changedTask);
        Task taskFromDB = repo.findById(task.getId()).get();
        assertThat(update).isTrue();
        assertThat(taskFromDB.getDescription()).isEqualTo(changedTask.getDescription());
        assertThat(taskFromDB.getCreated()).isEqualTo(changedTask.getCreated());
        assertThat(taskFromDB.isDone()).isEqualTo(changedTask.isDone());
    }

    @Test
    public void whenDeleteThenFindAllReturnsListWithSizeZero() {
        TaskRepository repo = new TaskRepository(sf);
        Task task = new Task();
        task.setDescription("Task");
        task.setDone(false);
        repo.add(task);
        boolean delete = repo.delete(task);
        assertThat(delete).isTrue();
        assertThat(repo.findAll().size()).isEqualTo(0);
    }
}