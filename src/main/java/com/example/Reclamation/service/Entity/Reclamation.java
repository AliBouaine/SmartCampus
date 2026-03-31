package com.example.Reclamation.service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sujet;
    private String description;
    private String status;

    public Reclamation() {
    }

    public Reclamation(Long id, String status, String sujet, String description) {
        this.id = id;
        this.status = status;
        this.sujet = sujet;
        this.description = description;
    }

    public Reclamation(String sujet, String description, String status) {
        this.sujet = sujet;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
