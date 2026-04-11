package com.example.Reservation.messaging;

 // ← même classe que dans User Service
import com.example.Reservation.Entity.UserCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    @RabbitListener(queues = "formation-certificat.queue")
    public void onUserCreated(UserCreatedEvent event) {
        System.out.println("📩 [Gestion de Classe] Nouvel utilisateur reçu : "
                + event.getUsername() + " (" + event.getEmail() + ")");
        // Tu peux ajouter ici ta logique métier (créer une classe par défaut, etc.)
    }
}