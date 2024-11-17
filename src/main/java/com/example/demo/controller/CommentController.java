package com.example.demo.controller;

import com.example.demo.dto.comment.*;
import com.example.demo.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.util.Constants.COMMENTS_PATH;


@RestController
@RequestMapping(COMMENTS_PATH)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comments", description = "Comment management API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new comment")
    public CommentResponseDTO createComment(@Valid @RequestBody CommentCreateDTO dto) {
        return commentService.createComment(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update comment")
    public CommentResponseDTO updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateDTO dto) {
        return commentService.updateComment(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete comment")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
