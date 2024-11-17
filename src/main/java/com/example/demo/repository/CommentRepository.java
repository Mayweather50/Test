package com.example.demo.repository;

import com.example.demo.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Получение комментариев для определенной задачи с пагинацией
    List<Comment> findByTaskId(Long taskId);

    // Получение всех комментариев к задаче, упорядоченных по дате создания (от новых к старым)
    List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);

    // Удаление всех комментариев для определенной задачи
    void deleteByTaskId(Long taskId);

    // Найти комментарии, написанные определённым автором
    List<Comment> findByAuthorId(Long authorId);
}


