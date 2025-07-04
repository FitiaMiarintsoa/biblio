package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface PersonneRepository extends JpaRepository<Personne, Integer> {}
