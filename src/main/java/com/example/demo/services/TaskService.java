package com.example.demo.services;

import com.example.demo.dto.task.*;
import com.example.demo.entity.*;
import com.example.demo.entity.User;
import com.example.demo.enums.*;
import com.example.demo.exception.*;
import com.example.demo.mapper.*;
import com.example.demo.repository.*;
import jakarta.validation.ValidationException;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toDto);
    }


    public Page<TaskResponseDTO> searchTasks(
            String query,
            TaskStatus status,
            TaskPriority priority,
            Long assigneeId,
            int page,
            int size,
            String[] sort) {

        Specification<Task> spec = Specification.where(null);

        if (StringUtils.hasText(query)) {
            spec = spec.and((root, q, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("title")), "%" + query.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("description")), "%" + query.toLowerCase() + "%")
                    )
            );
        }

        if (status != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        }

        if (priority != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("priority"), priority));
        }

        if (assigneeId != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("assignee").get("id"), assigneeId));
        }

        Sort sortObj = createSort(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);

        return taskRepository.findAll(spec, pageRequest).map(taskMapper::toDto);
    }
    private Sort createSort(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortParam : sort) {
            String[] parts = sortParam.split(",");
            String property = parts[0];
            Sort.Direction direction = parts.length > 1 ? Sort.Direction.fromString(parts[1]) : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, property));
        }

        return Sort.by(orders);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }
    public List<TaskResponseDTO> getCurrentUserTasks() {
        User currentUser = userRepository.getCurrentUser();
        List<Task> tasks = taskRepository.findByAssigneeId(currentUser.getId(), Pageable.unpaged()).getContent();
        return tasks.stream().map(taskMapper::toDto).toList();
    }

    @Transactional
    public TaskResponseDTO create(TaskCreateDTO dto) {
        User currentUser = userRepository.getCurrentUser();
        Task task = taskMapper.toEntity(dto);
        task.setAuthor(currentUser);

        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        task.setStatus(dto.getStatus());

        return taskMapper.toDto(taskRepository.save(task));
    }


    @Transactional
    public TaskResponseDTO update(Long id, TaskUpdateDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User currentUser = userRepository.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) &&
                !task.getAuthor().getId().equals(currentUser.getId()) &&
                !task.getAssignee().getId().equals(currentUser.getId())) {
            throw new ValidationException("You don't have permission to update this task");
        }

        taskMapper.updateTaskFromDto(dto, task);
        return taskMapper.toDto(taskRepository.save(task));
    }
    public TaskResponseDTO updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Transactional
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User currentUser = userRepository.getCurrentUser();
        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) &&
                !task.getAuthor().getId().equals(currentUser.getId())) {
            throw new ValidationException("You don't have permission to delete this task");
        }

        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponseDTO assignTask(Long taskId, Long assigneeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + assigneeId));

        task.setAssignee(assignee);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toDto(savedTask);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getTasksWithFilters(TaskFilterDTO filters, Pageable pageable) {
        Page<Task> tasks = taskRepository.findWithFilters(
                filters.getStatus(),
                filters.getPriority(),
                filters.getAuthorId(),
                filters.getAssigneeId(),
                pageable
        );
        return tasks.map(taskMapper::toDto);
    }


}
