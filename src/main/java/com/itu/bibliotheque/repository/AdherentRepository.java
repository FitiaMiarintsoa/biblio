package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Personne;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
    Optional<Adherent> findByPersonne(Personne personne);   
}
