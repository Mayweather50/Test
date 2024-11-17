package com.example.demo.mapper;

import com.example.demo.dto.task.*;
import com.example.demo.entity.*;
import org.springframework.stereotype.*;

@Component
public class TaskMapper {

    public  TaskResponseDTO toDto(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .author(UserMapper.toDTO(task.getAuthor()))
                .assignee(task.getAssignee() != null ? UserMapper.toDTO(task.getAssignee()) : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public Task toEntity(TaskCreateDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        return task;
    }

    public void updateTaskFromDto(TaskUpdateDTO dto, Task task) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
    }
}

