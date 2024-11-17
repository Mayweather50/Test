package com.example.demo.repository;

import com.example.demo.entity.*;
import com.example.demo.enums.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    // Найти задачи, созданные определенным автором с пагинацией
    Page<Task> findByAuthorId(Long authorId, Pageable pageable);
    Optional<Task> findByCommentsId(Long commentId);

    // Найти задачи, назначенные определенному исполнителю с пагинацией
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    // Поиск задач с фильтрацией по статусу, приоритету, автору или исполнителю
    @Query("SELECT t FROM Task t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:authorId IS NULL OR t.author.id = :authorId) AND " +
            "(:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    Page<Task> findWithFilters(
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("authorId") Long authorId,
            @Param("assigneeId") Long assigneeId,
            Pageable pageable
    );

    // Подсчёт количества задач с определённым статусом
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);
}


