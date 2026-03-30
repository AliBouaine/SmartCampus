package com.example.note.service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String etudiant;
    private double note;

    public Note(Long id, String etudiant, double note) {
        this.id = id;
        this.etudiant = etudiant;
        this.note = note;
    }
    public Note() {}
    public Note(String etudiant, double note) {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(String etudiant) {
        this.etudiant = etudiant;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
