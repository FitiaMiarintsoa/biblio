package com.itu.bibliotheque.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itu.bibliotheque.model.TypeSanction;

public interface TypeSanctionRepository extends JpaRepository<TypeSanction, Integer> {
    Optional<TypeSanction> findByNom(String nom);
}

