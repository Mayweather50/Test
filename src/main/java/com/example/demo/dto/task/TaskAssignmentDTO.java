package com.example.demo.dto.task;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentDTO {
    @NotNull(message = "User ID is required")
    private Long userId;
}

