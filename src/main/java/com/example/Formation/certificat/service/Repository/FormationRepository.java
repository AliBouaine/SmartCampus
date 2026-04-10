package com.example.Formation.certificat.service.Repository;

import com.example.Formation.certificat.service.Entity.Formation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FormationRepository extends MongoRepository<Formation, String> {
    List<Formation> findByCategorie(String categorie);
    List<Formation> findByTitreContainingIgnoreCase(String titre);
}