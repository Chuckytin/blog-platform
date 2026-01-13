package com.example.backend.repositories;

import com.example.backend.domain.PostStatus;
import com.example.backend.domain.entities.Category;
import com.example.backend.domain.entities.Post;
import com.example.backend.domain.entities.Tag;
import com.example.backend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByPostStatusAndCategoryAndTagsContaining(PostStatus postStatus, Category category, Tag tag);
    List<Post> findAllByPostStatusAndCategory(PostStatus postStatus, Category category);
    List<Post> findAllByPostStatusAndTagsContaining(PostStatus postStatus, Tag tag);
    List<Post> findAllByPostStatus(PostStatus postStatus);
    List<Post> findAllByAuthorAndPostStatus(User author, PostStatus postStatus);

}