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
    private static final String FIND_ALL = "FROM Task";
    private static final String DELETE_BY_ID = "DELETE Task WHERE id = :fId";
    private final SessionFactory sf;

    public Task add(Task task) {
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Exception when adding Task into DB: ", exc);
        }
        return task;
    }

    public Optional<Task> findById(int id) {
        Optional<Task> result = Optional.empty();
        try (Session session = sf.openSession()) {
            Query<Task> query = session.createQuery(FIND_BY_ID, Task.class)
                    .setParameter("fId", id);
            result = Optional.of(query.uniqueResult());
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


    public boolean update(Task task) {
        boolean result = false;
        Transaction transaction = null;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            session.merge(task);
            transaction.commit();
            result = true;
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Exception when update Task into DB: ", exc);
        }
        return result;
    }

    public boolean delete(Task task) {
        boolean result = false;
        Transaction transaction = null;
        int update;
        try (Session session = sf.openSession()) {
            transaction = session.beginTransaction();
            update = session.createMutationQuery(DELETE_BY_ID)
                            .setParameter("fId", task.getId()).executeUpdate();
            transaction.commit();
            result = update > 0;
        } catch (Exception exc) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Exception when delete Task from DB: ", exc);
        }
        return result;
    }
}
