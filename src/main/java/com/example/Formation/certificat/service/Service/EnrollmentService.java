package com.example.Formation.certificat.service.Service;

import com.example.Formation.certificat.service.Entity.Enrollment;
import com.example.Formation.certificat.service.Entity.Formation;
import com.example.Formation.certificat.service.Repository.EnrollmentRepository;
import com.example.Formation.certificat.service.Repository.FormationRepository;
import com.example.Formation.certificat.service.enums.EnrollmentStatus;
import com.example.Formation.certificat.service.event.EnrollmentRequest;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final FormationRepository formationRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             FormationRepository formationRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.formationRepository = formationRepository;
    }

    public Enrollment inscrire(EnrollmentRequest request) {
        formationRepository.findById(request.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation introuvable: " + request.getFormationId()));

        enrollmentRepository.findByUserIdAndFormationId(request.getUserId(), request.getFormationId())
                .ifPresent(e -> { throw new RuntimeException("User déjà inscrit à cette formation"); });

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(request.getUserId());
        enrollment.setFormationId(request.getFormationId());
        enrollment.setDateInscription(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollment.setProgression(new ArrayList<>());

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getByUser(String userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    public Enrollment getById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment introuvable: " + id));
    }

    public void autoEnroll(String userId, String role) {
        List<Formation> formations = formationRepository.findByCategorie(role);
        if (formations.isEmpty()) return;

        Formation formation = formations.get(0);
        boolean dejaInscrit = enrollmentRepository
                .findByUserIdAndFormationId(userId, formation.getId())
                .isPresent();

        if (!dejaInscrit) {
            Enrollment e = new Enrollment();
            e.setUserId(userId);
            e.setFormationId(formation.getId());
            e.setDateInscription(LocalDateTime.now());
            e.setStatus(EnrollmentStatus.ENROLLED);
            e.setProgression(new ArrayList<>());
            enrollmentRepository.save(e);
            System.out.println("Auto-enrollment: user " + userId + " → " + formation.getTitre());
        }
    }
}