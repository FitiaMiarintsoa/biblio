package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Abonnement;
import com.itu.bibliotheque.model.Adherent;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Integer> {
    boolean existsByAdherentAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
    Adherent adherent, LocalDate dateDebut, LocalDate dateFin);

}
