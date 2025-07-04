package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Integer> {
}
