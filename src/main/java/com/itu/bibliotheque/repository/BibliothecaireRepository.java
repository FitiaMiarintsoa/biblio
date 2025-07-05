package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Bibliothecaire;
import com.itu.bibliotheque.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BibliothecaireRepository extends JpaRepository<Bibliothecaire, Integer> {
    Optional<Bibliothecaire> findByPersonne(Personne personne);
}
