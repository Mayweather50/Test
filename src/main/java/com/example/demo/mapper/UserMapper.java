package com.example.demo.mapper;

import com.example.demo.dto.user.*;
import com.example.demo.entity.*;
import org.springframework.stereotype.*;

@Component
public class UserMapper {
    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
    public static User toEntity(UserResponseDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }

}
