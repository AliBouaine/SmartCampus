package com.example.course.service.Messaging;

import com.example.course.service.config.RabbitMQConfig;
import com.example.course.service.dto.CourseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger log = LoggerFactory.getLogger(CourseProducer.class);

    public CourseProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCourse(CourseDTO courseDTO) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.COURSE_QUEUE, courseDTO);
            log.info("Course envoyé à la queue : {}", courseDTO);
        } catch (AmqpException e) {
            log.error("Erreur lors de l'envoi du course à RabbitMQ", e);
            throw e;
        }
    }
}

