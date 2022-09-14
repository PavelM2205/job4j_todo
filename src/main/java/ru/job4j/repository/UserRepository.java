package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);
    private static final String FIND_BY_ID = "FROM User WHERE id = :fId";
    private static final String FIND_ALL = "FROM User";
    private static final String FIND_BY_LOGIN_AND_PASSWORD =
            "FROM User WHERE login = :fLogin AND password = :fPassword";
    private final SessionFactory sf;

    public Optional<User> addUser(User user) {
        Optional<User> result = Optional.empty();
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            result = Optional.of(user);
            session.close();
        } catch (Exception exc) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            if (exc.getCause() instanceof ConstraintViolationException) {
                throw new IllegalArgumentException("User with this login already exists");
            }
            LOG.error("Exception when adding User into DB: ", exc);
        }
        return result;
    }

    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            Query<User> query = session.createQuery(FIND_BY_ID, User.class)
                    .setParameter("fId", id);
            result = query.uniqueResultOptional();
        } catch (Exception exc) {
            LOG.error("Exception when findById User from DB: ", exc);
        }
        return result;
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            result = session.createQuery(FIND_ALL, User.class).list();
        } catch (Exception exc) {
            LOG.error("Exception when findAll User from DB: ", exc);
        }
        return result;
    }

    public boolean update(User user) {
        boolean result = false;
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            result = true;
            session.close();
        } catch (Exception exc) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            if (exc.getCause() instanceof  ConstraintViolationException) {
                throw new IllegalArgumentException("User with this login already exists");
            }
            LOG.error("Exception when update User into DB: ", exc);
        }
        return result;
    }

    public boolean delete(User user) {
        boolean result = false;
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
            result = true;
            session.close();
        } catch (Exception exc) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            LOG.error("Exception when delete User from DB: ", exc);
        }
        return result;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            Query<User> query = session.createQuery(FIND_BY_LOGIN_AND_PASSWORD, User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password);
            result = query.uniqueResultOptional();
        } catch (Exception exc) {
            LOG.error("Exception when findByLoginAndPassword User from DB: ", exc);
        }
        return result;
    }
}
