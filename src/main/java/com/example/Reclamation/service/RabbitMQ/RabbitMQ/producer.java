package com.example.Reclamation.service.RabbitMQ.RabbitMQ;

import com.example.Reclamation.service.Entity.Reclamation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class producer {

    private static final String EXCHANGE = "reclamation.exchange";
    private static final String ROUTING_KEY = "reclamation.created";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendReclamation(Reclamation reclamation) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, reclamation);
        System.out.println("✅ Reclamation envoyée vers RabbitMQ : " + reclamation);
    }
}