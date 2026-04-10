package com.example.Formation.certificat.service.Repository;

import com.example.Formation.certificat.service.Entity.Certificat;
import com.example.Formation.certificat.service.enums.CertificatStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CertificatRepository extends MongoRepository<Certificat, String> {
    List<Certificat> findByUserId(String userId);
    Optional<Certificat> findByNumeroUnique(String numeroUnique);
    List<Certificat> findByUserIdAndStatus(String userId, CertificatStatus status);
    boolean existsByUserIdAndFormationId(String userId, String formationId);
}