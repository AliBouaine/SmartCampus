package com.example.Formation.certificat.service.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "formations")
@Data
public class Formation {

    @Id
    private String id;
    private String titre;
    private int duree;
    private String description;
    private String categorie;
    private List<Module> modules;
    private int scoreMinCertification;

    public Formation(String titre, int duree, String description,
                     String categorie, List<Module> modules,
                     int scoreMinCertification)
    {
        this.titre = titre;
        this.duree = duree;
        this.description = description;
        this.categorie = categorie;
        this.modules = modules;
        this.scoreMinCertification = scoreMinCertification;
    }

    public Formation() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public int getScoreMinCertification() {
        return scoreMinCertification;
    }

    public void setScoreMinCertification(int scoreMinCertification) {
        this.scoreMinCertification = scoreMinCertification;
    }




}
