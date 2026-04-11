    package com.example.note.service.Controller;

    import com.example.note.service.Entity.Note;
    import com.example.note.service.Repository.NoteRepository;
    import com.example.note.service.Service.BulletinPdfService;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/bulletin")
    public class BulletinController {

        private final BulletinPdfService pdfService;
        private final NoteRepository noteRepository;

        public BulletinController(BulletinPdfService pdfService,
                                  NoteRepository noteRepository) {
            this.pdfService = pdfService;
            this.noteRepository = noteRepository;
        }

        @GetMapping("/etudiant/{etudiantId}")
        public ResponseEntity<byte[]> getBulletin(@PathVariable Long etudiantId) {
            try {
                List<Note> notes = noteRepository.findByEtudiantId(etudiantId);
                if (notes.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                byte[] pdf = pdfService.genererBulletin(notes, etudiantId);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(
                        ContentDisposition.attachment()
                                .filename("bulletin_etudiant_" + etudiantId + ".pdf")
                                .build()
                );
                return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }
    }
