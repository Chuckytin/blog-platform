package com.example.backend.controllers;

import com.example.backend.domain.dtos.CreateTagsRequest;
import com.example.backend.domain.dtos.TagResponse;
import com.example.backend.domain.entities.Tag;
import com.example.backend.mappers.TagMapper;
import com.example.backend.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagResponse> tagResponse = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponse);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest) {
        List<Tag> savedTagS = tagService.createTags(createTagsRequest.getNames());
        List<TagResponse> createTagsResponse = savedTagS.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(
                createTagsResponse,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}
