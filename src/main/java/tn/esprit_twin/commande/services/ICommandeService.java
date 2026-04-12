package tn.esprit_twin.commande.services;

import tn.esprit_twin.commande.entities.Commande;

import java.util.List;
import java.util.Optional;

public interface ICommandeService {

    List<Commande> getAllCommandes();

    Optional<Commande> getCommandeById(String id);

    Commande createCommande(Commande commande);

    Commande updateCommande(String id, Commande commandeDetails);

    void deleteCommande(String id);

    List<Commande> getCommandesByClientId(String clientId);

    List<Commande> getCommandesByStatut(String statut);
}