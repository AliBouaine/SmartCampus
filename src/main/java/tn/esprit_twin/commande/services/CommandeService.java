package tn.esprit_twin.commande.services;

import tn.esprit_twin.commande.entities.Commande;
import tn.esprit_twin.commande.repositories.CommandeRepository;
import tn.esprit_twin.commande.dto.CommandeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommandeService implements ICommandeService {

    @Autowired
    private CommandeRepository repository;

    @Autowired
    private CommandeProducer commandeProducer;

    // Liste toutes les commandes
    @Override
    public List<Commande> getAllCommandes() {
        return repository.findAll();
    }

    // Récupère une commande par id
    @Override
    public Optional<Commande> getCommandeById(String id) {
        return repository.findById(id);
    }

    // Crée une nouvelle commande + envoi message RabbitMQ
    @Override
    public Commande createCommande(Commande commande) {

        // Initialisation automatique
        commande.setStatut("En cours");
        commande.setDateCommande(new Date());

        // 1️⃣ Sauvegarde MongoDB
        Commande savedCommande = repository.save(commande);

        // 2️⃣ Création du DTO pour RabbitMQ
        CommandeDTO commandeDTO = new CommandeDTO(
                savedCommande.getId(),
                savedCommande.getClientId(),
                savedCommande.getTotal(),
                savedCommande.getAdresseLivraison()
        );

        // 3️⃣ Envoi vers RabbitMQ
        commandeProducer.sendCommande(commandeDTO);

        return savedCommande;
    }

    // Met à jour une commande existante
    @Override
    public Commande updateCommande(String id, Commande commandeDetails) {
        return repository.findById(id).map(commande -> {

            commande.setClientId(commandeDetails.getClientId());
            commande.setTotal(commandeDetails.getTotal());
            commande.setStatut(commandeDetails.getStatut());
            commande.setDateCommande(commandeDetails.getDateCommande());
            commande.setAdresseLivraison(commandeDetails.getAdresseLivraison());
            commande.setModePaiement(commandeDetails.getModePaiement());

            return repository.save(commande);

        }).orElseThrow(() -> new RuntimeException("Commande non trouvée avec id " + id));
    }

    // Supprime une commande
    @Override
    public void deleteCommande(String id) {
        repository.deleteById(id);
    }

    // Liste des commandes par client
    @Override
    public List<Commande> getCommandesByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }

    // Liste des commandes par statut
    @Override
    public List<Commande> getCommandesByStatut(String statut) {
        return repository.findByStatut(statut);
    }
}