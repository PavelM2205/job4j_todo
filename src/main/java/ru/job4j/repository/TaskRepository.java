package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TaskRepository.class);
    private static final String FIND_BY_ID = "FROM Task as t WHERE t.id = :fId";
    private static final String UPDATE = "UPDATE Task SET name = :fName, "
            + "description = :fDescription, created = :fCreated, done = :fDone WHERE id = :fId";
    private static final String FIND_ALL = "FROM Task";
    private static final String DELETE_BY_ID = "DELETE Task WHERE id = :fId";
    private static final String FIND_ALL_DONE = "FROM Task WHERE done = true";
    private static final String FIND_ALL_UNDONE = "FROM Task WHERE done = false";
    private static final String SET_DONE = "UPDATE Task SET done = true WHERE id = :fId";
    private final SessionFactory sf;

    public Optional<Task> add(Task task) {
        Optional<Task> result = Optional.empty();
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            result = Optional.of(task);
        } catch (Exception exc) {
            LOG.error("Exception when adding Task into DB: ", exc);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return result;
    }

    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            Query<Task> query = session.createQuery(FIND_BY_ID, Task.class)
                    .setParameter("fId", id);
            result = query.uniqueResultOptional();
        } catch (Exception exc) {
            LOG.error("Exception when findById Task into DB: ", exc);
        }
        return result;
    }

    public List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            result = session.createQuery(FIND_ALL, Task.class).list();
        } catch (Exception exc) {
            LOG.error("Exception when findAll from DB: ", exc);
        }
        return result;
    }

    public List<Task> findAllDone() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            result = session.createQuery(FIND_ALL_DONE, Task.class).list();
        } catch (Exception exc) {
            LOG.error("Exception when findAllDone from DB: ", exc);
        }
        return result;
    }

    public List<Task> findAllUndone() {
        List<Task> result = new ArrayList<>();
        try (Session session = sf.openSession()) {
            result = session.createQuery(FIND_ALL_UNDONE, Task.class).list();
        } catch (Exception exc) {
            LOG.error("Exception when findAllUndone from DB: ", exc);
        }
        return result;
    }

    public boolean update(Task task) {
        boolean result = false;
        Session session = null;
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
            result = true;
            session.close();
        } catch (Exception exc) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            LOG.error("Exception when update Task into DB: ", exc);
        }
        return result;
    }

    public boolean setDone(int id) {
        Transaction transaction = null;
        boolean result = false;
        int update;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            update = session.createMutationQuery(SET_DONE)
                    .setParameter("fId", id)
                    .executeUpdate();
            transaction.commit();
            result = update > 0;
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Exception when setDone into DB: ", exc);
        }
        return result;
    }

    public boolean delete(int id) {
        boolean result = false;
        Transaction transaction = null;
        int update;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            update = session.createMutationQuery(DELETE_BY_ID)
                            .setParameter("fId", id).executeUpdate();
            transaction.commit();
            result = update > 0;
        } catch (Exception exc) {
            LOG.error("Exception when delete Task from DB: ", exc);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return result;
    }
}
