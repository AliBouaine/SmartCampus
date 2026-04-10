package com.example.Formation.certificat.service.event;

import java.time.LocalDateTime;

public class CertificatResponse {
    private String id;
    private String userId;
    private String formationId;
    private String numeroUnique;
    private LocalDateTime dateEmission;
    private LocalDateTime dateExpiration;
    private int scoreObtenu;
    private String status;
    private String formationTitre;

    public CertificatResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFormationId() { return formationId; }
    public void setFormationId(String formationId) { this.formationId = formationId; }

    public String getNumeroUnique() { return numeroUnique; }
    public void setNumeroUnique(String numeroUnique) { this.numeroUnique = numeroUnique; }

    public LocalDateTime getDateEmission() { return dateEmission; }
    public void setDateEmission(LocalDateTime dateEmission) { this.dateEmission = dateEmission; }

    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; }

    public int getScoreObtenu() { return scoreObtenu; }
    public void setScoreObtenu(int scoreObtenu) { this.scoreObtenu = scoreObtenu; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFormationTitre() { return formationTitre; }
    public void setFormationTitre(String formationTitre) { this.formationTitre = formationTitre; }
}