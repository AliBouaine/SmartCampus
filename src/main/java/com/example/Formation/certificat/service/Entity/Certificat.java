package com.example.Formation.certificat.service.Entity;

import com.example.Formation.certificat.service.enums.CertificatStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "certificats")
public class Certificat {
    @Id
    private String id;
    private String userId;
    private String formationId;
    private String enrollmentId;
    private String numeroUnique;     // UUID généré
    private LocalDateTime dateEmission;
    private int scoreObtenu;
    private CertificatStatus status; // VALIDE, EXPIRE, REVOQUE
    private LocalDateTime dateExpiration;

    public Certificat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }

    public String getNumeroUnique() {
        return numeroUnique;
    }

    public void setNumeroUnique(String numeroUnique) {
        this.numeroUnique = numeroUnique;
    }

    public LocalDateTime getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    public int getScoreObtenu() {
        return scoreObtenu;
    }

    public void setScoreObtenu(int scoreObtenu) {
        this.scoreObtenu = scoreObtenu;
    }

    public CertificatStatus getStatus() {
        return status;
    }

    public void setStatus(CertificatStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    // ex: +2 ans
}
