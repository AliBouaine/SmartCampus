package com.example.Reservation.Controller;

import com.example.Reservation.Entity.Reservation;
import com.example.Reservation.Service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    // Constructor Injection (أفضل طريقة)
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reservation> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Reservation one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Reservation add(@RequestBody Reservation r) {
        return service.add(r);
    }

    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id,
                              @RequestBody Reservation r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/test-rabbit")
    public String testRabbit() {
        service.sendMessage();
        return "Message Sent";
    }
    @PostMapping("/{userId}")
    public Reservation createWithUser(@PathVariable String userId, @RequestBody Reservation r) {
        return service.add(r, userId);   // Appel avec userId via OpenFeign
    }
    // Recherche avancée - TESTABLE avec Postman
    @GetMapping("/search")
    public List<Reservation> search(
            @RequestParam(required = false) String salle,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String statut) {
        return service.searchReservations(salle, date, statut);
    }

}