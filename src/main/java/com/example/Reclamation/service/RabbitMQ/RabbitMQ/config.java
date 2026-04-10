package com.example.Reclamation.service.RabbitMQ.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class config {

    // === Pour les réclamations ===
    public static final String RECLAMATION_EXCHANGE = "reclamation.exchange";
    public static final String RECLAMATION_QUEUE = "reclamation.queue";
    public static final String RECLAMATION_ROUTING_KEY = "reclamation.created";

    // === Pour les événements User (si tu veux recevoir "user créé") ===
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";

    @Bean
    public TopicExchange reclamationExchange() {
        return new TopicExchange(RECLAMATION_EXCHANGE);
    }

    @Bean
    public Queue reclamationQueue() {
        return new Queue(RECLAMATION_QUEUE, true); // durable
    }

    @Bean
    public Binding reclamationBinding(Queue reclamationQueue, TopicExchange reclamationExchange) {
        return BindingBuilder.bind(reclamationQueue)
                .to(reclamationExchange)
                .with(RECLAMATION_ROUTING_KEY);
    }

    // Configuration pour User (optionnel pour l'instant)
    @Bean
    public Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, true);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userCreatedQueue)
                .to(userExchange)
                .with(USER_CREATED_ROUTING_KEY);
    }
}