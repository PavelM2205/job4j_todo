package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.model.Category;
import ru.job4j.repository.CategoryRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(int id) {
        Optional<Category> optCategory = categoryRepository.findById(id);
        if (optCategory.isEmpty()) {
            throw new NoSuchElementException("Category with such id does not exist");
        }
        return optCategory.get();
    }

    public List<Category> findSeveralCategoriesById(List<Integer> ids) {
        return categoryRepository.findSeveralCategoriesById(ids);
    }
}
