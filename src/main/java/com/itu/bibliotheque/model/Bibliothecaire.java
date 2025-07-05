package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bibliothecaire")
public class Bibliothecaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_personne", nullable = false)
    private Personne personne;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @Column(name = "date_suppression")
    private LocalDateTime dateSuppression;

    // Getters & Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
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
