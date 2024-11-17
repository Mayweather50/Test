package com.example.demo.dto.task;

import com.example.demo.enums.*;
import lombok.*;

import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterDTO {
    private Long authorId;
    private Long assigneeId;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private String searchTerm;
}
