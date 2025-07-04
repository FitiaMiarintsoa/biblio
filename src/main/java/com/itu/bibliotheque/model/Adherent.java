package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adherent")
public class Adherent extends Personne {

    private String identifiant;

    @ManyToOne
    @JoinColumn(name = "id_categorie_adherent")
    private CategorieAdherent categorieAdherent;

    @Column(name = "quota_max")
    private Integer quotaMax;

    @Column(name = "est_bloque")
    private Boolean estBloque;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @Column(name = "date_suppression")
    private LocalDateTime dateSuppression;

    // NOUVEAU champ motDePasse
    @Column(name = "mot_de_passe")
    private String motDePasse;

    // --- Getters & Setters ---

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public CategorieAdherent getCategorieAdherent() {
        return categorieAdherent;
    }

    public void setCategorieAdherent(CategorieAdherent categorieAdherent) {
        this.categorieAdherent = categorieAdherent;
    }

    public Integer getQuotaMax() {
        return quotaMax;
    }

    public void setQuotaMax(Integer quotaMax) {
        this.quotaMax = quotaMax;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }

    public LocalDateTime getDateSuppression() {
        return dateSuppression;
    }

    public void setDateSuppression(LocalDateTime dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
