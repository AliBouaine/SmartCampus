package com.example.Reservation.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // ✅ On utilise exactement les mêmes constantes que Formation et User Service
    public static final String EXCHANGE = "formation-certificat.exchange";
    public static final String QUEUE = "formation-certificat.queue";
    public static final String ROUTING_KEY = "formation-certificat.routing";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);   // durable
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);   // durable
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();   // corrigé (ancien nom causait des erreurs)
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter());
        return template;
    }
}