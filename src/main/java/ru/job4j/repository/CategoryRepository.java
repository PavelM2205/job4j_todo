package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CategoryRepository {
    private static final String FIND_ALL = "FROM Category";
    private static final String FIND_BY_ID = "FROM Category WHERE id = :fId";
    private static final String FIND_SEVERAL_BY_IDS =
            "FROM Category WHERE id IN (:fIds)";
    private final CrudRepository crudRepository;

    public List<Category> findAll() {
        return crudRepository.query(FIND_ALL, Category.class);
    }

    public Optional<Category> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID, Category.class, Map.of("fId", id));
    }

    public List<Category> findSeveralCategoriesById(List<Integer> ids) {
        return crudRepository.query(FIND_SEVERAL_BY_IDS, Category.class,
                Map.of("fIds", ids));
    }
}
