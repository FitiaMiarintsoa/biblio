package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuration_jour_ouvre")
public class ConfigurationJourOuvre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direction_decalage", nullable = false)
    private String directionDecalage; // "avant" ou "apres"

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    // --- Getters et Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirectionDecalage() {
        return directionDecalage;
    }

    public void setDirectionDecalage(String directionDecalage) {
        this.directionDecalage = directionDecalage;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
