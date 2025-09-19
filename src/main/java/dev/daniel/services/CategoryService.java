package dev.daniel.services;

import dev.daniel.repository.CategoryRepository;
import dev.daniel.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category saveCategory(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }

        Category category = new Category();
        category.setName(name.trim());

        return categoryRepository.save(category);
    }


    public int countCategories(String filter) {
        if (filter == null || filter.isBlank()) {
            return (int) categoryRepository.count();
        }
        return Integer.parseInt(categoryRepository.countByNameContainingIgnoreCase(filter));
    }


    public List<Category> findCategories(String filter, int offset, int limit) {
        if (filter == null || filter.isBlank()) {
            return categoryRepository.findAll()
                    .stream()
                    .toList();
        }

        return categoryRepository.findByNameContainingIgnoreCase(filter)
                .stream()
                .toList();
    }
}
