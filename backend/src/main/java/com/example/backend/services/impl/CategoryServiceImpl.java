package com.example.backend.services.impl;

import com.example.backend.domain.entities.Category;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio de gestión de categorías.
 * - Lista todas las categorías con el número de posts.
 * - Crea nuevas categorías verificando duplicados.
 * - Elimina categorías si no tienen posts asociados.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Lista todas las categorías y la cantidad de posts asociados a cada una.
     */
    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    /**
     * Crea una nueva categoría y lanza error si ya existe una con el mismo nombre.
     */
    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }
        return categoryRepository.save(category);

    }

    /**
     * Elimina una categoría por su ID si no tiene posts adicionales.
     * Lanza error si existen posts relacionados.
     */
    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            if (!categoryOptional.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

}