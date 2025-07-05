package com.itu.bibliotheque.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itu.bibliotheque.model.Role;
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNom(String nom);
}
