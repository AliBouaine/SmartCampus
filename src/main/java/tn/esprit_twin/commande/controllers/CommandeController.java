package tn.esprit_twin.commande.controllers;

import tn.esprit_twin.commande.entities.Commande;
import tn.esprit_twin.commande.services.CommandeService;
import tn.esprit_twin.commande.dto.CommandeDTO;
import tn.esprit_twin.commande.services.CommandeProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService service;

    @Autowired
    private CommandeProducer commandeProducer;

    @GetMapping
    public List<Commande> getAllCommandes() {
        return service.getAllCommandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable String id) {
        return service.getCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Commande createCommande(@RequestBody Commande commande) {
        return service.createCommande(commande);
    }

    @PutMapping("/{id}")
    public Commande updateCommande(@PathVariable String id, @RequestBody Commande commande) {
        return service.updateCommande(id, commande);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Commande> patchCommande(
            @PathVariable String id,
            @RequestBody Commande commande) {
        return service.getCommandeById(id).map(existing -> {
            if (commande.getStatut() != null) {
                existing.setStatut(commande.getStatut());
            }
            return ResponseEntity.ok(service.updateCommande(id, existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable String id) {
        service.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public List<Commande> getCommandesByClient(@PathVariable String clientId) {
        return service.getCommandesByClientId(clientId);
    }

    @GetMapping("/statut/{statut}")
    public List<Commande> getCommandesByStatut(@PathVariable String statut) {
        return service.getCommandesByStatut(statut);
    }

    @PostMapping("/test-producer/{id}")
    public ResponseEntity<String> sendCommandeToRabbit(@PathVariable String id) {
        return service.getCommandeById(id).map(commande -> {
            CommandeDTO dto = new CommandeDTO(
                    commande.getId(),
                    commande.getClientId(),
                    commande.getTotal(),
                    commande.getAdresseLivraison()
            );
            commandeProducer.sendCommande(dto);
            return ResponseEntity.ok("Message envoyé vers RabbitMQ ✅");
        }).orElse(ResponseEntity.notFound().build());
    }
}