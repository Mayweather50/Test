package com.example.demo.handler;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseBody {
    private String message;
    private String description;
}

