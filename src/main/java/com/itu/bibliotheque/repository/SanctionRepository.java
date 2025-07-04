package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Sanction;
import com.itu.bibliotheque.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SanctionRepository extends JpaRepository<Sanction, Integer> {
    List<Sanction> findByAdherentAndEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
        Adherent adherent, LocalDate today1, LocalDate today2
    );
}
