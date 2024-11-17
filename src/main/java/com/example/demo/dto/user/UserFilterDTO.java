package com.example.demo.dto.user;

import com.example.demo.enums.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDTO {
    private String email;
    private String name;
    private Role role;
}
