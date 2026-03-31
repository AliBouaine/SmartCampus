package com.example.Reclamation.service.Service;

import com.example.Reclamation.service.Entity.Reclamation;
import com.example.Reclamation.service.Repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository repo;

    public List<Reclamation> getAll() {
        return repo.findAll();
    }

    public Reclamation getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Reclamation add(Reclamation r) {
        return repo.save(r);
    }

    public Reclamation update(Long id, Reclamation r) {
        Reclamation old = getById(id);
        if (old != null) {
            old.setSujet(r.getSujet());
            old.setDescription(r.getDescription());
            old.setStatus(r.getStatus());
            return repo.save(old);
        }
        return null;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
