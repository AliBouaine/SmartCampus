package com.example.Reservation.Service;

import com.example.Reservation.Entity.*;
import com.example.Reservation.Repository.ReservationRepository;
import com.example.Reservation.Repository.UserClient;
import com.example.Reservation.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository repo;

    public List<Reservation> getAll() {
        return repo.findAll();
    }

    public Reservation getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Reservation add(Reservation r) {
        return repo.save(r);
    }

   /* public Reservation update(Long id, Reservation r) {
        Reservation old = getById(id);
        if (old != null) {
            old.setNom(r.getNom());
            old.setDate(r.getDate());
            return repo.save(old);
        }
        return null;
    }*/
   public Reservation update(Long id, Reservation r) {

       Reservation old = repo.findById(id).orElse(null);

       if (old != null) {

           old.setSalle(r.getSalle());
           old.setDate(r.getDate());
           old.setHeureDebut(r.getHeureDebut());
           old.setHeureFin(r.getHeureFin());
           old.setEquipement(r.getEquipement());
           old.setStatut(r.getStatut());

           return repo.save(old);
       }

       return null;
   }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    @Autowired
    private UserClient userClient;
    @Autowired
    private  RabbitTemplate rabbitTemplate;

    public Reservation add(Reservation r, String userId){

        // 🔵 FEIGN CALL
        UserDTO user = userClient.getUserById(userId);

        if(user == null)
            throw new RuntimeException("User not found");

        r.setStatut(StatutReservation.RESERVEE);

        Reservation saved = repo.save(r);

        // 🔴 RABBIT EVENT
        EducationEvent event = new EducationEvent();
        event.setType("RESERVATION_CREATED");
        event.setMessage("Reservation id: " + saved.getId());

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );

        return saved;
    }


//    public void sendMessage() {
//
//        String message = "Reservation Created Successfully 🔥";
//
//        rabbitTemplate.convertAndSend(
//                RabbitConfig.EXCHANGE,
//                RabbitConfig.ROUTING_KEY,
//                message
//        );
//
//        System.out.println("Message Sent: " + message);
//    }

    public void sendMessage() {

        // Simulation d'un vrai événement comme celui envoyé par User Service
        UserCreatedEvent event = new UserCreatedEvent(
                "12345",
                "testuser",
                "test@email.com",
                "STUDENT",
                ""
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );

        System.out.println("✅ Message Sent (UserCreatedEvent) : " + event);
    }

    // Métier avancé : Recherche filtrée (sans changer le code existant)
    public List<Reservation> searchReservations(String salle, String date, String statut) {
        return repo.findAll().stream()
                .filter(r -> salle == null || r.getSalle().toLowerCase().contains(salle.toLowerCase()))
                .filter(r -> date == null || r.getDate().toString().contains(date))
                .filter(r -> statut == null || r.getStatut().toString().equals(statut))
                .toList();
    }

}
