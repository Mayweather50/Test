package com.example.demo.dto.auth;

import com.example.demo.dto.user.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private UserResponseDTO user;


}