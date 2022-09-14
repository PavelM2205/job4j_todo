package ru.job4j.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
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
    void whenFindByLoginAndPasswordReturnsOptionalEmptyThenMustBeException() {
        UserRepository store = new UserRepository(sf);
        UserService service = new UserService(store);
        assertThrows(IllegalArgumentException.class, () ->
                service.findByLoginAndPassword("login", "password"));
    }
}