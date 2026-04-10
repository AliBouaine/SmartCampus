package com.example.Reclamation.service.Controller;

import com.example.Reclamation.service.Entity.Reclamation;
import com.example.Reclamation.service.Service.Service.ReclamationService;
import com.example.Reclamation.service.Service.Service.ReclamationPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/reclamations")
@RequiredArgsConstructor
@Slf4j
public class ReclamationPdfController {

    private final ReclamationService reclamationService;
    private final ReclamationPdfService pdfService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable String id,
            @RequestParam(defaultValue = "false") boolean inline) {

        Optional<Reclamation> opt = reclamationService.getById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Reclamation rec = opt.get();

        try {
            byte[] pdfBytes = pdfService.generatePdf(rec);

            String filename = "reclamation_" + id + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            inline ? "inline; filename=" + filename
                                    : "attachment; filename=" + filename)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("Erreur PDF id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}