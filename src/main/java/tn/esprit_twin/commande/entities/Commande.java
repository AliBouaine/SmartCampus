package tn.esprit_twin.commande.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "commandes")
public class Commande {

    @Id
    private String id;

    private String clientId;              // Identifiant du client
    private double total;                 // Montant total
    private String statut;                // "En cours", "Livrée", "Annulée"
    private Date dateCommande;            // Date de création
    private String adresseLivraison;      // Adresse de livraison
    private String modePaiement;          // Mode de paiement
}