package com.example.Formation.certificat.service.Service;

import com.example.Formation.certificat.service.Entity.Enrollment;
import com.example.Formation.certificat.service.Entity.Formation;
import com.example.Formation.certificat.service.Entity.ModuleProgress;
import com.example.Formation.certificat.service.Repository.EnrollmentRepository;
import com.example.Formation.certificat.service.Repository.FormationRepository;
import com.example.Formation.certificat.service.enums.EnrollmentStatus;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProgressService {

    private final EnrollmentRepository enrollmentRepository;
    private final FormationRepository formationRepository;

    public ProgressService(EnrollmentRepository enrollmentRepository,
                           FormationRepository formationRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.formationRepository = formationRepository;
    }

    public Enrollment completerModule(String enrollmentId, String moduleId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment introuvable: " + enrollmentId));

        boolean dejaComplete = enrollment.getProgression().stream()
                .anyMatch(p -> p.getModuleId().equals(moduleId) && p.isComplete());

        if (!dejaComplete) {
            ModuleProgress progress = new ModuleProgress();
            progress.setModuleId(moduleId);
            progress.setComplete(true);
            progress.setDateCompletion(LocalDateTime.now());
            enrollment.getProgression().add(progress);
        }

        if (enrollment.getStatus() == EnrollmentStatus.ENROLLED) {
            enrollment.setStatus(EnrollmentStatus.IN_PROGRESS);
        }

        Formation formation = formationRepository.findById(enrollment.getFormationId())
                .orElseThrow();

        long modulesCompletes = enrollment.getProgression().stream()
                .filter(ModuleProgress::isComplete).count();

        if (formation.getModules() != null &&
                modulesCompletes >= formation.getModules().size()) {
            System.out.println("Tous les modules complétés pour enrollment: " + enrollmentId);
        }

        return enrollmentRepository.save(enrollment);
    }

    public double getProgression(String enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment introuvable"));

        Formation formation = formationRepository.findById(enrollment.getFormationId())
                .orElseThrow();

        if (formation.getModules() == null || formation.getModules().isEmpty()) return 0;

        long completes = enrollment.getProgression().stream()
                .filter(ModuleProgress::isComplete).count();

        return (double) completes / formation.getModules().size() * 100;
    }
}