package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE        = "formation-certificat.exchange";
    public static final String QUEUE           = "formation-certificat.queue";
    public static final String ROUTING_KEY     = "formation-certificat.routing";

    // ✅ Nouvelle queue pour visualisation
    public static final String USER_QUEUE      = "user.queue";
    public static final String USER_ROUTING_KEY = "user.created";

    private final ConnectionFactory connectionFactory;

    public RabbitConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding userBinding() {
        return BindingBuilder.bind(userQueue())
                .to(exchange()).with(USER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter());
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @PostConstruct
    public void initRabbit() {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareExchange(new TopicExchange(EXCHANGE, true, false));
        admin.declareQueue(new Queue(QUEUE, true));
        admin.declareQueue(new Queue(USER_QUEUE, true));
        admin.declareBinding(
                BindingBuilder.bind(new Queue(QUEUE, true))
                        .to(new TopicExchange(EXCHANGE, true, false))
                        .with(ROUTING_KEY)
        );
        admin.declareBinding(
                BindingBuilder.bind(new Queue(USER_QUEUE, true))
                        .to(new TopicExchange(EXCHANGE, true, false))
                        .with(USER_ROUTING_KEY)
        );
        System.out.println("✅ RabbitMQ initialisé : exchange=" + EXCHANGE);
        System.out.println("✅ Queues : " + QUEUE + " | " + USER_QUEUE);
    }
}