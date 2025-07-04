package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Integer> {
    List<Exemplaire> findByStatut(String statut);
    Optional<Exemplaire> findFirstByIdLivreAndStatut(Integer idLivre, String statut);
}