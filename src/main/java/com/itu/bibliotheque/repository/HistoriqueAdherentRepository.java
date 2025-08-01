
package com.itu.bibliotheque.repository;


import com.itu.bibliotheque.model.HistoriqueAdherent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoriqueAdherentRepository extends JpaRepository<HistoriqueAdherent, Integer> {
    List<HistoriqueAdherent> findByAdherentIdOrderByDateActionDesc(Long adherentId);
}

