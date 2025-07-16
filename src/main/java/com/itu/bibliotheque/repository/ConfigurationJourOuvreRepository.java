package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.ConfigurationJourOuvre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationJourOuvreRepository extends JpaRepository<ConfigurationJourOuvre, Long> {
    ConfigurationJourOuvre findTopByOrderByIdDesc(); // on récupère la dernière config (ou la seule)
}
