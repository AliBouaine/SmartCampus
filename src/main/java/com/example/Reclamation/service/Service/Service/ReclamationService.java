package com.example.Reclamation.service.Service.Service;

import com.example.Reclamation.service.Entity.Reclamation;
import com.example.Reclamation.service.Entity.UserInfo;
import com.example.Reclamation.service.Repository.ReclamationRepository;
import com.example.Reclamation.service.RabbitMQ.config.config;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReclamationService {

    private final ReclamationRepository reclamationRepo;
    private final RestClient.Builder restClientBuilder;
    private final RabbitTemplate rabbitTemplate;

    // ✅ URL correcte du user-service
    @Value("${user-service.url:http://localhost:8090}")
    private String userServiceUrl;

    // =========================
    // GET ALL
    // =========================
    public List<Reclamation> getAll() {
        return reclamationRepo.findAll();
    }

    // =========================
    // GET BY ID
    // =========================
    public Optional<Reclamation> getById(String id) {
        try {
            return reclamationRepo.findById(Long.parseLong(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // =========================
    // ADD (with user enrichment)
    // =========================
    public Reclamation add(Reclamation reclamation, String userId) {

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId est obligatoire");
        }

        UserInfo userInfo = getUserInfo(userId);

        if (userInfo == null) {
            throw new RuntimeException("Utilisateur introuvable avec id: " + userId);
        }

        reclamation.setUserId(userId);
        reclamation.setUsername(userInfo.getUsername());
        reclamation.setUserEmail(userInfo.getEmail());

        return reclamationRepo.save(reclamation);
    }

    // =========================
    // ADD + RABBITMQ
    // =========================
    public Reclamation addWithRabbit(Reclamation reclamation, String userId, Logger log) {

        Reclamation saved = add(reclamation, userId);

        rabbitTemplate.convertAndSend(
                config.RECLAMATION_EXCHANGE,
                config.RECLAMATION_ROUTING_KEY,
                saved.getId() + ":" + userId
        );

        log.info("Réclamation envoyée via RabbitMQ : ID={}", saved.getId());

        return saved;
    }

    // =========================
    // UPDATE
    // =========================
    public Reclamation update(String id, Reclamation reclamation) {
        reclamation.setId(Long.parseLong(id));
        return reclamationRepo.save(reclamation);
    }

    // =========================
    // DELETE
    // =========================
    public void delete(String id) {
        reclamationRepo.deleteById(Long.parseLong(id));
    }

    // =========================
    // CALL USER SERVICE
    // =========================
    private UserInfo getUserInfo(String userId) {

        RestClient restClient = restClientBuilder.build();

        try {
            return restClient.get()
                    .uri(userServiceUrl + "/api/users/" + userId)
                    .retrieve()
                    .body(UserInfo.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erreur lors de l'appel au User Service (URL=" + userServiceUrl + ") : "
                            + e.getMessage()
            );
        }
    }

    public byte[] generatePdf(Reclamation rec) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            doc.add(new Paragraph("TEST PDF OK"));
            doc.add(new Paragraph("Titre: " + rec.getTitre()));

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}