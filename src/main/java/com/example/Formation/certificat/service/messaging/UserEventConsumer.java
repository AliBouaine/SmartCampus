package com.example.Formation.certificat.service.messaging;

import com.example.Formation.certificat.service.event.UserCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    @RabbitListener(queues = "formation-certificat.queue")
    public void onUserCreated(UserCreatedEvent event) {
        System.out.println("📩 [Formation-Service] User reçu : " + event.getUsername());
    }
}