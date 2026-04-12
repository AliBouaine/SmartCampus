package tn.esprit_twin.commande.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit_twin.commande.ConfigRabbit.RabbitMQConfig;
import tn.esprit_twin.commande.dto.CommandeDTO;


@Service
public class CommandeProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger log = LoggerFactory.getLogger(CommandeProducer.class);

    // Injection du RabbitTemplate (configuré avec Jackson2JsonMessageConverter)
    public CommandeProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCommande(CommandeDTO commandeDTO) {

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.COMMANDE_QUEUE,
                    commandeDTO
            );

            log.info("Commande envoyée à la queue {} : {}",
                    RabbitMQConfig.COMMANDE_QUEUE,
                    commandeDTO);

        } catch (AmqpException e) {

            log.error("Erreur lors de l'envoi de la commande à RabbitMQ", e);

            // On remonte l'exception pour signaler l’échec
            throw e;
        }
    }
}
