package com.example.backend.services;

import com.example.backend.domain.CreatePostRequest;
import com.example.backend.domain.entities.Post;
import com.example.backend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPost(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
}
