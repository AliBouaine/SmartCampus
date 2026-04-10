package com.example.Formation.certificat.service.Controller;

import com.example.Formation.certificat.service.Service.CertificatService;
import com.example.Formation.certificat.service.event.CertificatResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificats")
public class CertificatController {

    private final CertificatService certificatService;

    public CertificatController(CertificatService certificatService) {
        this.certificatService = certificatService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CertificatResponse>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(certificatService.getByUser(userId));
    }

    @GetMapping("/{numeroUnique}/verify")
    public ResponseEntity<?> verifier(@PathVariable String numeroUnique) {
        try {
            CertificatResponse response = certificatService.verifier(numeroUnique);
            return ResponseEntity.ok(Map.of(
                    "valide", response.getStatus().equals("VALIDE"),
                    "certificat", response
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "valide", false,
                    "message", "Certificat introuvable"
            ));
        }
    }
}