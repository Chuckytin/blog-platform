package com.example.backend.repositories;

import com.example.backend.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    /**
     * Recupera todos los tags y sus posts asociados usando JOIN FETCH.
     */
    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllWithAllPostCount();

    /**
     * Busca todos los tags cuyo nombre está incluido en el conjunto de nombres dado.
     */
    List<Tag> findByNameIn(Set<String> names);

}