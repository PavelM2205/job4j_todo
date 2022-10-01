package ru.job4j.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class UserRepositoryTest {
    private static StandardServiceRegistry registry =
            new StandardServiceRegistryBuilder().configure().build();
    private static SessionFactory sf = new MetadataSources(registry).buildMetadata()
            .buildSessionFactory();

    @BeforeAll
    public static void cleanTableBefore() {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE Task").executeUpdate();
            session.createMutationQuery("DELETE User").executeUpdate();
            transaction.commit();
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw exc;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @AfterEach
    public void cleanTable() {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE User").executeUpdate();
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

    @Test
    public void whenAddUser() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        User user = new User();
        user.setName("Pavel");
        user.setLogin("login");
        user.setPassword("password");
        store.addUser(user);
        User userFromDB = store.findById(user.getId()).get();
        assertThat(user.getId()).isNotEqualTo(0);
        assertThat(userFromDB.getId()).isEqualTo(user.getId());
        assertThat(userFromDB.getName()).isEqualTo(user.getName());
        assertThat(userFromDB.getLogin()).isEqualTo(user.getLogin());
        assertThat(userFromDB.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void whenAddUserThenReturnsOptionalUserWithId() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        User user = new User();
        user.setName("Pavel");
        user.setLogin("login");
        user.setPassword("password");
        User result = store.addUser(user).get();
        assertThat(result.getId()).isNotEqualTo(0);
    }

    @Test
    public void whenAddTwoUsersThenFindAllReturnsBoth() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        User user1 = new User();
        user1.setName("Ivan");
        user1.setLogin("login1");
        user1.setPassword("password1");
        User user2 = new User();
        user2.setName("Petr");
        user2.setLogin("login2");
        user2.setPassword("password2");
        store.addUser(user1);
        store.addUser(user2);
        List<User> usersFromDB = store.findAll();
        assertThat(usersFromDB.size()).isEqualTo(2);
        assertThat(usersFromDB.get(0).getId()).isEqualTo(user1.getId());
        assertThat(usersFromDB.get(0).getName()).isEqualTo(user1.getName());
        assertThat(usersFromDB.get(0).getLogin()).isEqualTo(user1.getLogin());
        assertThat(usersFromDB.get(0).getPassword()).isEqualTo(user1.getPassword());
        assertThat(usersFromDB.get(1).getId()).isEqualTo(user2.getId());
        assertThat(usersFromDB.get(1).getName()).isEqualTo(user2.getName());
        assertThat(usersFromDB.get(1).getLogin()).isEqualTo(user2.getLogin());
        assertThat(usersFromDB.get(1).getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    public void whenUpdate() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        User user = new User();
        user.setName("Pavel");
        user.setLogin("login");
        user.setPassword("password");
        store.addUser(user);
        User changed = new User();
        changed.setId(user.getId());
        changed.setName("Changed");
        changed.setLogin("changedLogin");
        changed.setPassword("changedPassword");
        store.update(changed);
        User userFromDB = store.findById(user.getId()).get();
        assertThat(userFromDB.getName()).isEqualTo(changed.getName());
        assertThat(userFromDB.getLogin()).isEqualTo(changed.getLogin());
        assertThat(userFromDB.getPassword()).isEqualTo(changed.getPassword());
    }

    @Test
    public void whenDelete() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        User user = new User();
        user.setName("Pavel");
        user.setLogin("login");
        user.setPassword("password");
        store.addUser(user);
        store.delete(user);
        assertThat(store.findAll().size()).isEqualTo(0);
    }

    @Test
    public void whenFindByLoginAndPassword() {
        CrudRepository cr = new CrudRepository(sf);
        UserRepository store = new UserRepository(cr);
        User user = new User();
        user.setName("Ivan");
        user.setLogin("login");
        user.setPassword("password");
        store.addUser(user);
        User userFromDB = store.findByLoginAndPassword(
                user.getLogin(), user.getPassword()).get();
        assertThat(userFromDB.getId()).isEqualTo(user.getId());
        assertThat(userFromDB.getLogin()).isEqualTo(user.getLogin());
        assertThat(userFromDB.getName()).isEqualTo(user.getName());
        assertThat(userFromDB.getPassword()).isEqualTo(user.getPassword());
    }
}