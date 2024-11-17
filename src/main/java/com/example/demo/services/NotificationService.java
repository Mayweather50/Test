package com.example.demo.services;

import com.example.demo.entity.*;
import com.example.demo.entity.User;
import com.example.demo.enums.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import jakarta.mail.internet.*;
import lombok.extern.slf4j.*;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@Transactional
@Slf4j
public class NotificationService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final JavaMailSender mailSender;

    public NotificationService(UserRepository userRepository, TaskRepository taskRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.mailSender = mailSender;
    }

    @Async
    public void notifyTaskAssignment(Long taskId, Long assigneeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new UserNotAuthorizedException("User not found"));

        String subject = "New Task Assignment";
        String content = String.format(
                "You have been assigned to task: %s\nPriority: %s\nDescription: %s",
                task.getTitle(),
                task.getPriority(),
                task.getDescription()
        );

        sendEmail(assignee.getEmail(), subject, content);
    }

    @Async
    public void notifyTaskStatusChange(Long taskId, TaskStatus oldStatus, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        String subject = "Task Status Changed";
        String content = String.format(
                "Task: %s\nStatus changed from %s to %s",
                task.getTitle(),
                oldStatus,
                newStatus
        );

        sendEmail(task.getAuthor().getEmail(), subject, content);
        if (task.getAssignee() != null) {
            sendEmail(task.getAssignee().getEmail(), subject, content);
        }
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }
    }
}
