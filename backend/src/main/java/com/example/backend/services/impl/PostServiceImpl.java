package com.example.backend.services.impl;

import com.example.backend.domain.PostStatus;
import com.example.backend.domain.entities.Category;
import com.example.backend.domain.entities.Post;
import com.example.backend.domain.entities.Tag;
import com.example.backend.domain.entities.User;
import com.example.backend.repositories.PostRepository;
import com.example.backend.services.CategoryService;
import com.example.backend.services.PostService;
import com.example.backend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByPostStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }

        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByPostStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByPostStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        return postRepository.findAllByPostStatus(PostStatus.PUBLISHED);

    }

    @Override
    public List<Post> getDraftPost(User user) {
        return postRepository.findAllByAuthorAndPostStatus(user, PostStatus.DRAFT);
    }

}
