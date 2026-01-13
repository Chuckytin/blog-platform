package com.example.backend.controllers;

import com.example.backend.domain.dtos.PostDto;
import com.example.backend.domain.entities.Post;
import com.example.backend.domain.entities.User;
import com.example.backend.mappers.PostMapper;
import com.example.backend.services.PostService;
import com.example.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {

        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        List<Post> draftPosts = postService.getDraftPost(user);
        List<PostDto> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

}
