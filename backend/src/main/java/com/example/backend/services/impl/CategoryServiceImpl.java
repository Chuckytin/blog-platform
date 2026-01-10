package com.example.backend.services.impl;

import com.example.backend.domain.entities.Category;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);

    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            if (!categoryOptional.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }

}