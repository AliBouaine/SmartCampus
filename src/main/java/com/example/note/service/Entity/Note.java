package com.example.note.service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matiere;
    private Double note;
    private Double coefficient;
    private String semestre;
    private Long etudiantId;
    private String nomEtudiant;
    private String prenomEtudiant;
}