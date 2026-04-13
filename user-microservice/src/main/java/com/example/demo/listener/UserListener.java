package com.example.demo.listener;

import com.example.demo.config.RabbitConfig;
import com.example.demo.entity.EducationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    @RabbitListener(queues = RabbitConfig.QUEUE) // même queue que RabbitConfig
    public void consume(EducationEvent event) {

        System.out.println("📩 USER SERVICE RECEIVED EVENT:");
        System.out.println("Type: " + event.getType());
        System.out.println("Message: " + event.getMessage());
    }
}