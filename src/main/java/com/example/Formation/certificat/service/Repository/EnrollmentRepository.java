package com.example.Formation.certificat.service.Repository;

import com.example.Formation.certificat.service.Entity.Enrollment;
import com.example.Formation.certificat.service.enums.EnrollmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    List<Enrollment> findByUserId(String userId);
    List<Enrollment> findByFormationId(String formationId);
    Optional<Enrollment> findByUserIdAndFormationId(String userId, String formationId);
    List<Enrollment> findByStatus(EnrollmentStatus status);
}