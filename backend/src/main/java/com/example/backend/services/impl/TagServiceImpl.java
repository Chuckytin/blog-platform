package com.example.backend.services.impl;

import com.example.backend.domain.entities.Tag;
import com.example.backend.repositories.TagRepository;
import com.example.backend.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de tags.
 * - Obtiene todos los tags con sus posts asociados.
 * - Crea tagas nuevos sin duplicar los existentes.
 * - Elimina tags si no están asociados a posts.
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /**
     * Recupera todos los tags con sus posts asociados.
     */
    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithAllPostCount();
    }

    /**
     * Crea nuevos tags a partir de un conjunto de nombres.
     * - Ignora los tags que ya existen.
     * - Devuelve la lista completa de tags resultantes (nuevos + existentes)
     */
    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);

        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    /**
     * Elimina un tag por ID si no tiene posts asociados.
     */
    @Transactional
    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with posts");
            }
            tagRepository.deleteById(id);
        });
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + id));
    }

}
