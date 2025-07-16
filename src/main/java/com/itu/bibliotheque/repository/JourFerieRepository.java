package com.itu.bibliotheque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itu.bibliotheque.model.JourFerie;

import java.time.LocalDate;

public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {
    boolean existsByDateFerie(LocalDate dateFerie);
}


