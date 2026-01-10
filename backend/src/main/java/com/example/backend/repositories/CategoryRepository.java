package com.example.backend.repositories;

import com.example.backend.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Obtiene todas las categorías con sus posts cargados en una sola consulta.
     * Usa JOIN FETCH para evitar el problema N+1 de consultas adicionales.
     * LEFT JOIN asegura que se incluyan categorías sin posts.
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    /**
     * Verifica si ya existe una categoría con el mismo nombre, ignorando mayúsculas/minúsculas.
     */
    boolean existsByNameIgnoreCase(String name);

}