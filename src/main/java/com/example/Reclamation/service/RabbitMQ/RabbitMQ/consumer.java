package com.example.Reclamation.service.RabbitMQ.RabbitMQ;

import com.example.Reclamation.service.Entity.Reclamation;
import com.example.Reclamation.service.Service.Service.ReclamationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class ReclamationConsumer {

    private final ReclamationService reclamationService;

    // Écoute quand un utilisateur est créé (venant du User Service)
    @RabbitListener(queues = "user.created.queue")
    public void handleUserCreated(String userId) {
        log.info(" [x] Utilisateur créé reçu via RabbitMQ → User ID: {}", userId);

        // Exemple : Tu peux créer une réclamation automatique
        // Reclamation reclamation = Reclamation.builder()
        //         .titre("Bienvenue")
        //         .description("Réclamation de bienvenue pour le nouvel utilisateur")
        //         .userId(userId)
        //         .build();
        // reclamationService.add(reclamation, userId);
    }

    // Écoute les réclamations (si un autre service envoie des réclamations)
    @RabbitListener(queues = "reclamation.queue")
    public void handleReclamationEvent(Reclamation reclamation) {
        log.info(" [x] Nouvelle réclamation reçue : {}", reclamation.getTitre());
    }
}