package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String id;   // 🔥 changed
    private String username;
    private String email;
    private String role;
    private String photo;
}