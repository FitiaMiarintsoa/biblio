package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Livre;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreRepository extends JpaRepository<Livre, Long> {
        Optional<Livre> findByIsbn(String isbn); 
}
