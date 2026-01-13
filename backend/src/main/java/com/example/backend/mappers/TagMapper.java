package com.example.backend.mappers;

import com.example.backend.domain.PostStatus;
import com.example.backend.domain.dtos.TagResponse;
import com.example.backend.domain.entities.Post;
import com.example.backend.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * MapStruct crea una bean de Spring.
 * IGNORE - Se ignoran los campos que no se puedan mapear.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    /**
     * Convierte un Tag a un TagResponse (DTO) y calcula los posts publicados.
     * target - campo destino del DTO
     * source - campo origen de la entidad
     * qualifiedByName - nombre del método a usar para transformarlo
     */
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse toTagResponse(Tag tag);

    /**
     * Cuenta solo posts con estado PUBLISHED.
     *
     * Named - nombra el método para reutilizarlo
     */
    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }

}
