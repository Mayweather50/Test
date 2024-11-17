package com.example.demo.services;

import com.example.demo.dto.comment.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.mapper.*;
import com.example.demo.repository.*;
import com.example.demo.security.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.security.access.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final CommentMapper commentMapper;

    public CommentResponseDTO createComment(CommentCreateDTO dto) {
        log.info("Creating new comment for task with id: {}", dto.getTaskId());

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + dto.getTaskId()));

        User currentUser = userService.getCurrentUser();

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setTask(task);
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);
        log.info("Created comment with id: {}", savedComment.getId());

        return commentMapper.toDTO(savedComment);
    }
    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        log.info("Fetching comments for task with id: {}", taskId);
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return commentMapper.toDTOList(comments);
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, CommentUpdateDTO dto) {
        log.info("Updating comment with id: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        User currentUser = userService.getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only update your own comments");
        }

        comment.setContent(dto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toDTO(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        User currentUser = userService.getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}
