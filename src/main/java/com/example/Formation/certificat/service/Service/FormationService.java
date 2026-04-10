package com.example.Formation.certificat.service.Service;

import com.example.Formation.certificat.service.Entity.Formation;
import com.example.Formation.certificat.service.Repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormationService {

    @Autowired
    private FormationRepository repo;

    public List<Formation> getAll() {
        return repo.findAll();
    }

    public Formation getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public Formation add(Formation f) {
        return repo.save(f);
    }

    public Formation update(String id, Formation f) {
        Formation old = getById(id);
        if (old != null) {
            old.setTitre(f.getTitre());
            old.setDuree(f.getDuree());
            return repo.save(old);
        }
        return null;
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
