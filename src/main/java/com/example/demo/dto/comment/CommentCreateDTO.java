package com.example.demo.dto.comment;

import com.example.demo.entity.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDTO {

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    private String content;

    private Long authorId;
    private Long taskId;

    public CommentCreateDTO(String content) {
        this.content = content;
    }
}


