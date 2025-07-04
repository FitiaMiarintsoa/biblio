package com.itu.bibliotheque.repository;
import com.itu.bibliotheque.model.TypeAction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TypeActionRepository extends JpaRepository<TypeAction, Integer> {
    Optional<TypeAction> findByNom(String nom);
}
