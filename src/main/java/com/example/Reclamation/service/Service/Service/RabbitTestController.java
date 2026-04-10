package com.example.Reclamation.service.Service.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rabbit-test")
@RequiredArgsConstructor
public class RabbitTestController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public String sendTestMessage(@RequestParam String message) {
        try {
            rabbitTemplate.convertAndSend(
                    com.example.Reclamation.service.RabbitMQ.config.config.RECLAMATION_EXCHANGE,
                    com.example.Reclamation.service.RabbitMQ.config.config.RECLAMATION_ROUTING_KEY,
                    message
            );

            log.info("Message envoyé avec succès : {}", message);
            return "✅ Message envoyé dans RabbitMQ : " + message;

        } catch (Exception e) {
            log.error("Erreur envoi RabbitMQ", e);
            return "❌ Erreur : " + e.getMessage();
        }
    }
}