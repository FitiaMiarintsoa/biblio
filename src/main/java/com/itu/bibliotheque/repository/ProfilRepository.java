package com.itu.bibliotheque.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itu.bibliotheque.model.Profil;

public interface ProfilRepository extends JpaRepository<Profil, Integer> {
    boolean existsByNomIgnoreCase(String nom);
}
