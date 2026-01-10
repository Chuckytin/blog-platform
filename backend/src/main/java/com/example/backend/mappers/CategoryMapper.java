package com.example.backend.mappers;

import com.example.backend.domain.PostStatus;
import com.example.backend.domain.dtos.CategoryDto;
import com.example.backend.domain.dtos.CreateCategoryRequest;
import com.example.backend.domain.entities.Category;
import com.example.backend.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct crea una bean de Spring.
 * IGNORE - Se ignoran los campos que no se puedan mapear.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    /**
     * Convierte Category a DTO. Calcula posts publicados.
     *
     * target - campo destino del DTO
     * source - campo origen de la entidad
     * qualifiedByName - nombre del método a usar para transformarlo
     */
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    /**
     * Convierte un DTO de creación de categoría (CreateCategoryRequest)
     * a la entidad Category que será persistida en la base de datos.
     */
    Category toEntity(CreateCategoryRequest createCategoryRequest);

    /**
     * Cuenta solo posts con estado PUBLISHED.
     *
     * Named - nombra el método para reutilizarlo
     */
    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (null == posts) {
            return 0;
        }
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getPostStatus()))
                .count();
    }

}
