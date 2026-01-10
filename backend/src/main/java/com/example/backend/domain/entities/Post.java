package com.example.backend.domain.entities;

import com.example.backend.domain.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tables")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Column(nullable = false)
    private Integer readingTime;

    /**
     * Autor del post. Lado propietario con la FK author_id.
     * Carga perezosa (LAZY) para mejor rendimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * Categoría del post. Lado propietario con la FK category_id.
     * Carga perezosa (LAZY) para mejor rendimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Tags del post. Set evita Tags duplicados en el mismo post.
     * {@link JoinTable} - crea tabla intermedia en la BBDD
     * JoinColumns - referencia a este post (post_id)
     * inverseJoinColumns - Referencia al tag telacionado (tag_id)
     */
    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(title, post.title) && Objects.equals(content, post.content) && postStatus == post.postStatus && Objects.equals(readingTime, post.readingTime) && Objects.equals(createdAt, post.createdAt) && Objects.equals(updateAt, post.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, postStatus, readingTime, createdAt, updateAt);
    }

    /**
     * Asigna fecha de creación y última actualización al crear un nuevo post.
     * {@link PrePersist} - solo se ejecuta en la primera persistencia
     */
    @PrePersist
    protected void onCreated() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updateAt = now;
    }

    /**
     * Actualiza la fecha de modificación en cada edición del post.
     * {@link PreUpdate} - se ejecuta en cada actualización.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

}
