package com.example.Reclamation.service.Entity;

import lombok.Data;

@Data
public class UserInfo {
    private String id;
    private String username;
    private String email;
    private String photo;   // optionnel
}