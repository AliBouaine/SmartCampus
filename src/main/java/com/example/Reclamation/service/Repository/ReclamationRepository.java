package com.example.Reclamation.service.Repository;

import com.example.Reclamation.service.Entity.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamationRepository
        extends JpaRepository<Reclamation, Long> {}
