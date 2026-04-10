package com.example.Formation.certificat.service.Service;

import com.example.Formation.certificat.service.Entity.Certificat;
import com.example.Formation.certificat.service.Entity.Enrollment;
import com.example.Formation.certificat.service.Entity.Formation;
import com.example.Formation.certificat.service.Repository.EnrollmentRepository;
import com.example.Formation.certificat.service.Repository.FormationRepository;
import com.example.Formation.certificat.service.enums.EnrollmentStatus;
import com.example.Formation.certificat.service.event.EvaluationRequest;

import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

    private final EnrollmentRepository enrollmentRepository;
    private final FormationRepository formationRepository;
    private final CertificatService certificatService;

    public EvaluationService(EnrollmentRepository enrollmentRepository,
                             FormationRepository formationRepository,
                             CertificatService certificatService) {
        this.enrollmentRepository = enrollmentRepository;
        this.formationRepository = formationRepository;
        this.certificatService = certificatService;
    }

    public Certificat soumettreEvaluation(EvaluationRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new RuntimeException("Enrollment introuvable: " + request.getEnrollmentId()));

        if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
            throw new RuntimeException("Évaluation déjà soumise pour cet enrollment");
        }

        Formation formation = formationRepository.findById(enrollment.getFormationId())
                .orElseThrow(() -> new RuntimeException("Formation introuvable"));

        enrollment.setScoreFinal(request.getScore());

        if (request.getScore() >= formation.getScoreMinCertification()) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollmentRepository.save(enrollment);
            return certificatService.generer(enrollment, request.getScore());
        } else {
            enrollment.setStatus(EnrollmentStatus.FAILED);
            enrollmentRepository.save(enrollment);
            System.out.println("Échec: score " + request.getScore() +
                    " < minimum " + formation.getScoreMinCertification());
            return null;
        }
    }
}