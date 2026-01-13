package com.example.backend.services;

import com.example.backend.domain.CreatePostRequest;
import com.example.backend.domain.UpdatePostRequest;
import com.example.backend.domain.entities.Post;
import com.example.backend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPost(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);

}
