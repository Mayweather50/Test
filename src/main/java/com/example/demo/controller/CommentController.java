package com.example.demo.controller;

import com.example.demo.dto.comment.*;
import com.example.demo.exception.*;
import com.example.demo.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @Operation(summary = "Create new comment", description = "Accessible by both admins and users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public CommentResponseDTO createComment(@Valid @RequestBody CommentCreateDTO dto) {
        return commentService.createComment(dto);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get comment by task ID", description = "Accessible by both admins and users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<CommentResponseDTO> getCommentsByTaskId(@PathVariable Long taskId) {
        return commentService.getCommentsByTaskId(taskId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update comment", description = "Only accessible by administrators")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CommentResponseDTO updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateDTO dto) {
        return commentService.updateComment(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete comment", description = "Only accessible by administrators")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @PostMapping("/{taskId}/comments")
    @Operation(summary = "Add a comment to a task", description = "Accessible by both admins and users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CommentResponseDTO> addCommentToTask(
            @PathVariable Long taskId,
            @RequestParam Long authorId,
            @Valid @RequestBody CommentCreateDTO dto) {

        CommentResponseDTO response = commentService.addCommentToTask(taskId, authorId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

