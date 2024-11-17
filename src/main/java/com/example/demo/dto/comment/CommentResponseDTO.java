package com.example.demo.dto.comment;

import com.example.demo.dto.user.*;
import com.example.demo.entity.*;
import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private UserResponseDTO author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long taskId;
}
