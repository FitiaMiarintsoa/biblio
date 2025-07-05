package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nom;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Utilisateur> utilisateurs;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Utilisateur> getUtilisateurs() { return utilisateurs; }
    public void setUtilisateurs(Set<Utilisateur> utilisateurs) { this.utilisateurs = utilisateurs; }
}
