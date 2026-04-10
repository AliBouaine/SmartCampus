package com.example.Formation.certificat.service.Controller;

import com.example.Formation.certificat.service.Entity.Certificat;
import com.example.Formation.certificat.service.Entity.Enrollment;
import com.example.Formation.certificat.service.Service.EnrollmentService;
import com.example.Formation.certificat.service.Service.EvaluationService;
import com.example.Formation.certificat.service.Service.ProgressService;
import com.example.Formation.certificat.service.event.EnrollmentRequest;
import com.example.Formation.certificat.service.event.EvaluationRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final ProgressService progressService;
    private final EvaluationService evaluationService;

    public EnrollmentController(EnrollmentService enrollmentService,
                                ProgressService progressService,
                                EvaluationService evaluationService) {
        this.enrollmentService = enrollmentService;
        this.progressService = progressService;
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<Enrollment> inscrire(@RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(enrollmentService.inscrire(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Enrollment>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(enrollmentService.getByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getById(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.getById(id));
    }

    @PutMapping("/{id}/module/{moduleId}")
    public ResponseEntity<Enrollment> completerModule(@PathVariable String id,
                                                      @PathVariable String moduleId) {
        return ResponseEntity.ok(progressService.completerModule(id, moduleId));
    }

    @GetMapping("/{id}/progression")
    public ResponseEntity<Map<String, Object>> getProgression(@PathVariable String id) {
        double pct = progressService.getProgression(id);
        return ResponseEntity.ok(Map.of(
                "enrollmentId", id,
                "progression", pct,
                "label", String.format("%.1f%%", pct)
        ));
    }

    @PostMapping("/evaluation")
    public ResponseEntity<?> soumettreEvaluation(@RequestBody EvaluationRequest request) {
        Certificat cert = evaluationService.soumettreEvaluation(request);
        if (cert != null) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Félicitations ! Certificat généré.",
                    "certificat", cert
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "Score insuffisant. Certification non obtenue."
            ));
        }
    }
}