package com.example.Formation.certificat.service.Entity;

import com.example.Formation.certificat.service.enums.EnrollmentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;
    private String userId;
    private String formationId;
    private LocalDateTime dateInscription;
    private EnrollmentStatus status; // ENROLLED, IN_PROGRESS, COMPLETED, FAILED
    private List<ModuleProgress> progression;
    private Integer scoreFinal;

    public String getId() {
        return id;
    }

    public Enrollment() {
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

    public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public List<ModuleProgress> getProgression() {
        return progression;
    }

    public void setProgression(List<ModuleProgress> progression) {
        this.progression = progression;
    }

    public Integer getScoreFinal() {
        return scoreFinal;
    }

    public void setScoreFinal(Integer scoreFinal) {
        this.scoreFinal = scoreFinal;
    }

    public Enrollment(String id, String userId, String formationId, LocalDateTime dateInscription, EnrollmentStatus status, List<ModuleProgress> progression, Integer scoreFinal) {
        this.id = id;
        this.userId = userId;
        this.formationId = formationId;
        this.dateInscription = dateInscription;
        this.status = status;
        this.progression = progression;
        this.scoreFinal = scoreFinal;
    }
}
