package com.example.demo.controller;

import com.example.demo.dto.comment.*;
import com.example.demo.dto.task.*;
import com.example.demo.entity.*;
import com.example.demo.enums.*;
import com.example.demo.services.*;
import com.example.demo.specification.TaskSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.demo.util.Constants.TASKS_PATH;


@RestController
@RequestMapping(TASKS_PATH)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tasks", description = "Task management API")
public class TaskController {

    private final TaskService taskService;


    @PostMapping
    @Operation(summary = "Create new task")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateDTO dto) {
        TaskResponseDTO createdTask = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        TaskResponseDTO task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO dto) {
        TaskResponseDTO updatedTask = taskService.update(id, dto);
        return ResponseEntity.ok(updatedTask);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @Operation(summary = "Get current user's tasks")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TaskResponseDTO>> getCurrentUserTasks() {
        List<TaskResponseDTO> tasks = taskService.getCurrentUserTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    @Operation(summary = "Search tasks by criteria")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<TaskResponseDTO>> searchTasks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        Page<TaskResponseDTO> tasks = taskService.searchTasks(query, status, priority, assigneeId, page, size, sort);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update task status")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO dto) {
        TaskResponseDTO updatedStatus = taskService.updateTaskStatus(id, dto.getStatus());
        return ResponseEntity.ok(updatedStatus);
    }

    @PutMapping("/{id}/assign")
    @Operation(summary = "Assign task to user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponseDTO> assignTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskAssignmentDTO dto) {
        TaskResponseDTO assignedTask = taskService.assignTask(id, dto.getUserId());
        return ResponseEntity.ok(assignedTask);
    }

    @PutMapping("/{id}/user/manage")
    @Operation(summary = "Manage task as user (update status or add comment)")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponseDTO> manageTaskAsUser(@PathVariable Long id,
                                                            @RequestBody TaskUpdateDTO dto,
                                                            @RequestParam(required = false) String comment) {
        CommentCreateDTO commentDto = comment != null ? new CommentCreateDTO(comment) : null;
        TaskResponseDTO updatedTask = taskService.manageTaskAsUser(id, dto, commentDto);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/admin/manage")
    @Operation(summary = "Manage task as admin (update status, priority, assign user, or add comment)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskResponseDTO> manageTaskAsAdmin(
            @PathVariable Long id,
            @RequestBody TaskUpdateDTO taskUpdateDTO,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) Long authorId) {

        CommentCreateDTO commentDto = null;
        if (comment != null) {
            if (authorId == null) {
                throw new ValidationException("Author ID must be provided when adding a comment.");
            }
            commentDto = new CommentCreateDTO(comment, authorId, id);
        }

        TaskResponseDTO updatedTask = taskService.manageTaskAsAdmin(id, taskUpdateDTO, commentDto);
        return ResponseEntity.ok(updatedTask);
    }


}
