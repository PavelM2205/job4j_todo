package ru.job4j.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.exception.UserWithSuchLoginAlreadyExists;
import ru.job4j.exception.UserWithSuchLoginAndPasswordDoesNotExists;
import ru.job4j.model.User;
import ru.job4j.repository.CrudRepository;
import ru.job4j.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private static SessionFactory sf;

    @BeforeAll
    public static void getSf() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @AfterEach
    public void cleanTable() {
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.createMutationQuery("DELETE User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception exc) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        }
    }

    @Test
    public void whenFindByLoginAndPasswordReturnsOptionalEmptyThenMustBeException() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        UserService service = new UserService(store);
        assertThrows(UserWithSuchLoginAndPasswordDoesNotExists.class, () ->
                service.findByLoginAndPassword("login", "password"));
    }

    @Test
    public void whenAddReturnsOptionalEmptyThenMustBeException() {
        UserRepository userStore = mock(UserRepository.class);
        UserService service = new UserService(userStore);
        User user = mock(User.class);
        when(userStore.addUser(user)).thenReturn(Optional.empty());
        assertThrows(UserWithSuchLoginAlreadyExists.class, () -> service.addUser(user));
    }

    @Test
    public void whenFindByIdReturnsOptionalEmptyThenMustBeException() {
        UserRepository userStore = mock(UserRepository.class);
        UserService service = new UserService(userStore);
        when(userStore.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.findById(1));
    }
}