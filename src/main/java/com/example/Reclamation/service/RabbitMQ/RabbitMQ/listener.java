package com.example.Reclamation.service.RabbitMQ.RabbitMQ;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class ReclamationListener {

    @RabbitListener(queues = "reclamation.queue") // nom de ta queue
    public void consume(String message) {

        System.out.println("📩 RECLAMATION RECEIVED:");
        System.out.println("Message: " + message);
    }
}