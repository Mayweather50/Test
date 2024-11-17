package com.example.demo.dto.task;

import com.example.demo.dto.comment.*;
import com.example.demo.dto.user.*;
import com.example.demo.enums.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserResponseDTO author;
    private UserResponseDTO assignee;
    private List<CommentResponseDTO> recentComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long totalComments;
}
