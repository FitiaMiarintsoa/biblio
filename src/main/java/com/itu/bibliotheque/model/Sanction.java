package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sanction")
public class Sanction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_adherent")
    private Adherent adherent;

    @Column(name = "type_sanction")
    private String typeSanction;

    private String description;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "est_active")
    private Boolean estActive = true;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout = LocalDateTime.now();

    @Column(name = "date_suppression")
    private LocalDateTime dateSuppression;

    // Getters & Setters
}
