package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;
import java.util.List;
import java.util.Map;
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
    private final CrudRepository crudRepository;

    public Optional<Task> add(Task task) {
        Optional<Task> result = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(task));
            result = Optional.of(task);
        } catch (Exception exc) {
            LOG.error("Exception when adding Task into DB: ", exc);
        }
        return result;
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Task.class, Map.of("fId", id));
    }

    public List<Task> findAll() {
        return crudRepository.query(FIND_ALL, Task.class);
    }

    public List<Task> findAllDone() {
        return crudRepository.query(FIND_ALL_DONE, Task.class);
    }

    public List<Task> findAllUndone() {
        return crudRepository.query(FIND_ALL_UNDONE, Task.class);
    }

    public void update(Task task) {
        crudRepository.run(session -> session.update(task));
    }

    public void setDone(int id) {
        crudRepository.run(SET_DONE, Map.of("fId", id));
    }

    public void delete(int id) {
        crudRepository.run(DELETE_BY_ID, Map.of("fId", id));
    }
}
