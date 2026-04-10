package com.example.Formation.certificat.service.Service;

import com.example.Formation.certificat.service.Entity.Certificat;
import com.example.Formation.certificat.service.Entity.Enrollment;
import com.example.Formation.certificat.service.Repository.CertificatRepository;
import com.example.Formation.certificat.service.Repository.FormationRepository;
import com.example.Formation.certificat.service.enums.CertificatStatus;
import com.example.Formation.certificat.service.event.CertificatResponse;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificatService {

    private final CertificatRepository certificatRepository;
    private final FormationRepository formationRepository;

    public CertificatService(CertificatRepository certificatRepository,
                             FormationRepository formationRepository) {
        this.certificatRepository = certificatRepository;
        this.formationRepository = formationRepository;
    }

    public Certificat generer(Enrollment enrollment, int score) {
        Certificat cert = new Certificat();
        cert.setUserId(enrollment.getUserId());
        cert.setFormationId(enrollment.getFormationId());
        cert.setEnrollmentId(enrollment.getId());
        cert.setNumeroUnique(UUID.randomUUID().toString().toUpperCase());
        cert.setDateEmission(LocalDateTime.now());
        cert.setDateExpiration(LocalDateTime.now().plusYears(2));
        cert.setScoreObtenu(score);
        cert.setStatus(CertificatStatus.VALIDE);

        Certificat saved = certificatRepository.save(cert);
        System.out.println("Certificat généré: " + saved.getNumeroUnique());
        return saved;
    }

    public List<CertificatResponse> getByUser(String userId) {
        return certificatRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CertificatResponse verifier(String numeroUnique) {
        Certificat cert = certificatRepository.findByNumeroUnique(numeroUnique)
                .orElseThrow(() -> new RuntimeException("Certificat introuvable: " + numeroUnique));

        if (cert.getStatus() == CertificatStatus.VALIDE &&
                cert.getDateExpiration().isBefore(LocalDateTime.now())) {
            cert.setStatus(CertificatStatus.EXPIRE);
            certificatRepository.save(cert);
        }

        return toResponse(cert);
    }

    private CertificatResponse toResponse(Certificat cert) {
        CertificatResponse response = new CertificatResponse();
        response.setId(cert.getId());
        response.setUserId(cert.getUserId());
        response.setFormationId(cert.getFormationId());
        response.setNumeroUnique(cert.getNumeroUnique());
        response.setDateEmission(cert.getDateEmission());
        response.setDateExpiration(cert.getDateExpiration());
        response.setScoreObtenu(cert.getScoreObtenu());
        response.setStatus(cert.getStatus().name());

        formationRepository.findById(cert.getFormationId())
                .ifPresent(f -> response.setFormationTitre(f.getTitre()));

        return response;
    }
}