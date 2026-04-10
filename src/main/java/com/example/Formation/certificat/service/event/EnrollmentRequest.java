package com.example.Formation.certificat.service.event;

public class EnrollmentRequest {
    private String userId;
    private String formationId;

    public EnrollmentRequest() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFormationId() { return formationId; }
    public void setFormationId(String formationId) { this.formationId = formationId; }
}