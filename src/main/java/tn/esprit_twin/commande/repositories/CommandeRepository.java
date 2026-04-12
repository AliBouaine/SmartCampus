package tn.esprit_twin.commande.repositories;

import tn.esprit_twin.commande.entities.Commande;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommandeRepository extends MongoRepository<Commande, String> {
    // Permet de récupérer toutes les commandes d’un client
    List<Commande> findByClientId(String clientId);

    // Permet de filtrer par statut
    List<Commande> findByStatut(String statut);
}