package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PriorityRepository {
    private final CrudRepository crudRepository;
    private static final String FIND_ALL = "FROM Priority";
    private static final String FIND_BY_ID = "FROM Priority WHERE id = :fId";

    public List<Priority> findAll() {
        return crudRepository.query(FIND_ALL, Priority.class);
    }

    public Optional<Priority> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Priority.class,
                Map.of("fId", id));
    }
}
