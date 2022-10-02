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
import ru.job4j.model.Category;
import ru.job4j.model.Priority;
import ru.job4j.model.Task;
import ru.job4j.model.User;

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

    @BeforeAll
    public static void cleanTableBefore() {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE Task").executeUpdate();
            session.createMutationQuery("DELETE USer").executeUpdate();
            transaction.commit();
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @AfterEach
    public void cleanTable() {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE Task").executeUpdate();
            session.createMutationQuery("DELETE User").executeUpdate();
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
        CrudRepository cr = new CrudRepository(sf);
        TaskRepository repo = new TaskRepository(cr);
        UserRepository userRepository = new UserRepository(cr);
        CategoryRepository categoryRepository = new CategoryRepository(cr);
        User user = new User();
        user.setName("Admin");
        user.setLogin("login");
        user.setPassword("password");
        userRepository.addUser(user);
        Priority priority = new Priority();
        priority.setId(1);
        priority.setName("high");
        priority.setPosition(1);
        Task task = new Task();
        task.setName("name");
        task.setDescription("Text");
        task.setDone(true);
        task.setUser(user);
        task.setPriority(priority);
        Category category = categoryRepository.findById(1).get();
        task.setCategories(List.of(category));
        repo.add(task);
        Task taskFromDB = repo.findById(task.getId()).get();
        assertThat(task.getId()).isNotEqualTo(0);
        assertThat(taskFromDB.getName()).isEqualTo(task.getName());
        assertThat(taskFromDB.getId()).isEqualTo(task.getId());
        assertThat(taskFromDB.getCreated()).isEqualToIgnoringNanos(task.getCreated());
        assertThat(taskFromDB.isDone()).isEqualTo(task.isDone());
        assertThat(taskFromDB.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskFromDB.getCategories().get(0).getId()).isEqualTo(category.getId());
        assertThat(taskFromDB.getCategories().get(0).getName()).isEqualTo(category.getName());
    }

    @Test
    public void whenAddTwoTasksAndFindAllThenReturnsBoth() {
        CrudRepository cr = new CrudRepository(sf);
        TaskRepository repo = new TaskRepository(cr);
        UserRepository userRepository = new UserRepository(cr);
        CategoryRepository categoryRepository = new CategoryRepository(cr);
        User user = new User();
        user.setName("Admin");
        user.setLogin("login");
        user.setPassword("password");
        userRepository.addUser(user);
        Priority priority1 = new Priority();
        priority1.setId(1);
        priority1.setName("high");
        priority1.setPosition(1);
        Priority priority2 = new Priority();
        priority2.setId(2);
        priority2.setName("normal");
        priority2.setPosition(2);
        Category category = categoryRepository.findById(1).get();
        Task task1 = new Task();
        task1.setName("1");
        task1.setDescription("task #1");
        task1.setDone(true);
        task1.setUser(user);
        task1.setPriority(priority1);
        task1.setCategories(List.of(category));
        Task task2 = new Task();
        task2.setDescription("task #2");
        task2.setName("2");
        task2.setDone(false);
        task2.setUser(user);
        task2.setPriority(priority2);
        task2.setCategories(List.of(category));
        repo.add(task1);
        repo.add(task2);
        List<Task> tasksFromDB = repo.findAll();
        assertThat(tasksFromDB.get(0).getId()).isEqualTo(task1.getId());
        assertThat(tasksFromDB.get(0).getName()).isEqualTo(task1.getName());
        assertThat(tasksFromDB.get(0).getDescription())
                .isEqualTo(task1.getDescription());
        assertThat(tasksFromDB.get(0).getCreated()).isEqualToIgnoringNanos(task1.getCreated());
        assertThat(tasksFromDB.get(0).isDone()).isEqualTo(task1.isDone());
        assertThat(tasksFromDB.get(0).getUser().getId()).isEqualTo(user.getId());
        assertThat(tasksFromDB.get(0).getUser().getName()).isEqualTo(user.getName());
        assertThat(tasksFromDB.get(0).getUser().getLogin()).isEqualTo(user.getLogin());
        assertThat(tasksFromDB.get(0).getUser().getPassword()).isEqualTo(user.getPassword());
        assertThat(tasksFromDB.get(0).getPriority().getId()).isEqualTo(priority1.getId());
        assertThat(tasksFromDB.get(0).getPriority().getName()).isEqualTo(priority1.getName());
        assertThat(tasksFromDB.get(0).getPriority().getPosition()).isEqualTo(priority1.getPosition());
        assertThat(tasksFromDB.get(0).getCategories().get(0).getId())
                .isEqualTo(category.getId());
        assertThat(tasksFromDB.get(0).getCategories().get(0).getName())
                .isEqualTo(category.getName());
        assertThat(tasksFromDB.get(1).getId()).isEqualTo(task2.getId());
        assertThat(tasksFromDB.get(1).getName()).isEqualTo(task2.getName());
        assertThat(tasksFromDB.get(1).getDescription())
                .isEqualTo(task2.getDescription());
        assertThat(tasksFromDB.get(1).getCreated()).isEqualToIgnoringNanos(task2.getCreated());
        assertThat(tasksFromDB.get(1).isDone()).isEqualTo(task2.isDone());
        assertThat(tasksFromDB.get(1).getUser().getId()).isEqualTo(user.getId());
        assertThat(tasksFromDB.get(1).getUser().getName()).isEqualTo(user.getName());
        assertThat(tasksFromDB.get(1).getUser().getLogin()).isEqualTo(user.getLogin());
        assertThat(tasksFromDB.get(1).getUser().getPassword()).isEqualTo(user.getPassword());
        assertThat(tasksFromDB.get(1).getPriority().getId()).isEqualTo(priority2.getId());
        assertThat(tasksFromDB.get(1).getPriority().getName()).isEqualTo(priority2.getName());
        assertThat(tasksFromDB.get(1).getPriority().getPosition()).isEqualTo(priority2.getPosition());
        assertThat(tasksFromDB.get(1).getCategories().get(0).getId())
                .isEqualTo(category.getId());
        assertThat(tasksFromDB.get(1).getCategories().get(0).getName())
                .isEqualTo(category.getName());
    }

    @Test
    public void whenUpdateThenMustBeChangedTask() {
        CrudRepository cr = new CrudRepository(sf);
        TaskRepository repo = new TaskRepository(cr);
        UserRepository userRepository = new UserRepository(cr);
        CategoryRepository categoryRepository = new CategoryRepository(cr);
        Priority priority1 = new Priority();
        priority1.setId(1);
        priority1.setName("high");
        priority1.setPosition(1);
        Priority priority2 = new Priority();
        priority2.setId(2);
        priority2.setName("normal");
        priority2.setPosition(2);
        Category category = categoryRepository.findById(1).get();
        User user = new User();
        user.setName("Admin");
        user.setLogin("login");
        user.setPassword("password");
        Task task = new Task();
        task.setName("t1");
        task.setDescription("Task");
        task.setDone(false);
        task.setPriority(priority1);
        task.setCategories(List.of(category));
        repo.add(task);
        Task changedTask = new Task();
        changedTask.setName("chName");
        changedTask.setDescription("Changed Task");
        changedTask.setDone(true);
        changedTask.setId(task.getId());
        changedTask.setPriority(priority2);
        changedTask.setCategories(List.of(category));
        repo.update(changedTask);
        Task taskFromDB = repo.findById(task.getId()).get();
        assertThat(taskFromDB.getName()).isEqualTo(changedTask.getName());
        assertThat(taskFromDB.getDescription()).isEqualTo(changedTask.getDescription());
        assertThat(taskFromDB.getCreated()).isEqualToIgnoringNanos(changedTask.getCreated());
        assertThat(taskFromDB.isDone()).isEqualTo(changedTask.isDone());
        assertThat(taskFromDB.getPriority().getId()).isEqualTo(priority2.getId());
        assertThat(taskFromDB.getPriority().getName()).isEqualTo(priority2.getName());
        assertThat(taskFromDB.getPriority().getPosition()).isEqualTo(priority2.getPosition());
        assertThat(taskFromDB.getCategories().get(0).getId()).isEqualTo(category.getId());
        assertThat(taskFromDB.getCategories().get(0).getName()).isEqualTo(category.getName());
    }

    @Test
    public void whenDeleteThenFindAllReturnsListWithSizeZero() {
        CrudRepository cr = new CrudRepository(sf);
        TaskRepository repo = new TaskRepository(cr);
        UserRepository userRepository = new UserRepository(cr);
        CategoryRepository categoryRepository = new CategoryRepository(cr);
        User user = new User();
        user.setName("Admin");
        user.setLogin("login");
        user.setPassword("password");
        userRepository.addUser(user);
        Category category = categoryRepository.findById(1).get();
        Priority priority = new Priority();
        priority.setId(1);
        priority.setName("high");
        priority.setPosition(1);
        Task task = new Task();
        task.setName("name");
        task.setDescription("Task");
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(List.of(category));
        repo.add(task);
        System.out.println("Result: " + repo.findById(task.getId()));
        repo.delete(task.getId());
        assertThat(repo.findAll().size()).isEqualTo(0);
    }

    @Test
    public void whenSetDoneThenDoneMustBeTrue() {
        CrudRepository cr = new CrudRepository(sf);
        TaskRepository repo = new TaskRepository(cr);
        UserRepository userRepository = new UserRepository(cr);
        CategoryRepository categoryRepository = new CategoryRepository(cr);
        Category category = categoryRepository.findById(1).get();
        User user = new User();
        user.setName("Admin");
        user.setLogin("login");
        user.setPassword("password");
        userRepository.addUser(user);
        Priority priority = new Priority();
        priority.setId(1);
        priority.setName("high");
        priority.setPosition(1);
        Task task = new Task();
        task.setName("name");
        task.setDescription("description");
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(List.of(category));
        repo.add(task);
        repo.setDone(task.getId());
        Task taskFromDB = repo.findById(task.getId()).get();
        assertThat(taskFromDB.isDone()).isTrue();
    }
}