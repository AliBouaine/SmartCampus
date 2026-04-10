package com.example.Formation.certificat.service.Entity;

public class Module {
    private String id;
    private String titre;
    private String contenu;
    private int ordre;

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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public Module() {
    }

    public Module(String id, String titre, String contenu, int ordre) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.ordre = ordre;
    }
}
