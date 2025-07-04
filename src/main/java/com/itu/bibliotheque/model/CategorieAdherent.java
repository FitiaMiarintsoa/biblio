package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "categorie_adherent")
public class CategorieAdherent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String description;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @Column(name = "date_suppression")
    private LocalDateTime dateSuppression;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getNom() {
      return nom;
    }

    public void setNom(String nom) {
      this.nom = nom;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
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

}