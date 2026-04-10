package com.example.Reclamation.service.Controller;

import com.example.Reclamation.service.Entity.Reclamation;
import com.example.Reclamation.service.Service.Service.ReclamationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reclamations")
@RequiredArgsConstructor
@Slf4j
public class ReclamationController {

    private final ReclamationService service;

    // ── GET ALL ──────────────────────────────────────────────────────────────
    @GetMapping
    public List<Reclamation> all() {
        return service.getAll();
    }

    // ── GET BY ID ────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> one(@PathVariable String id) {
        return service.getById(id)
                .map(rec -> {
                    String pdfUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/reclamations/{id}/pdf")
                            .buildAndExpand(id)
                            .toUriString();

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Link", "<" + pdfUrl + ">; rel=\"pdf\"");

                    return ResponseEntity.ok().headers(headers).body(rec);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ── CREATE ───────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<Reclamation> add(
            @RequestBody Reclamation r,
            @RequestParam String userId) {

        // Validation du userId
        if (userId == null || userId.isBlank()) {
            log.warn("Tentative de création sans userId");
            return ResponseEntity.badRequest().build();
        }

        try {
            Reclamation saved = service.add(r, userId);

            // Construction de l'URI de la ressource créée
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saved.getId())
                    .toUri();

            // Lien vers le PDF
            String pdfUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/reclamations/{id}/pdf")
                    .buildAndExpand(saved.getId())
                    .toUriString();

            log.info("Réclamation créée avec succès - ID: {}", saved.getId());

            return ResponseEntity
                    .created(location)
                    .header("Link", "<" + pdfUrl + ">; rel=\"pdf\"")
                    .body(saved);

        } catch (Exception e) {
            log.error("Erreur lors de la création de la réclamation pour userId={}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> update(@PathVariable String id, @RequestBody Reclamation r) {
        try {
            Reclamation updated = service.update(id, r);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de la réclamation id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        try {
            service.delete(id);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la réclamation id={}", id, e);
            // On laisse Spring renvoyer 500 si besoin
        }
    }
}