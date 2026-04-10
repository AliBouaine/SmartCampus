package com.example.Formation.certificat.service.Controller;

import com.example.Formation.certificat.service.Entity.Formation;
import com.example.Formation.certificat.service.Repository.FormationRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formations")
public class FormationController {

    private final FormationRepository formationRepository;

    public FormationController(FormationRepository formationRepository) {
        this.formationRepository = formationRepository;
    }

    @PostMapping
    public ResponseEntity<Formation> creer(@RequestBody Formation formation) {
        return ResponseEntity.ok(formationRepository.save(formation));
    }

    @GetMapping
    public ResponseEntity<List<Formation>> listerTout() {
        return ResponseEntity.ok(formationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formation> getById(@PathVariable String id) {
        return formationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<List<Formation>> getByCategorie(@PathVariable String categorie) {
        return ResponseEntity.ok(formationRepository.findByCategorie(categorie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formation> modifier(@PathVariable String id,
                                              @RequestBody Formation updated) {
        return formationRepository.findById(id).map(f -> {
            updated.setId(id);
            return ResponseEntity.ok(formationRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable String id) {
        formationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}