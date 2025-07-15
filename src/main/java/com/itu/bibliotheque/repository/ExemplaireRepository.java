package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Integer> {
    List<Exemplaire> findByStatut(String statut);
    Optional<Exemplaire> findFirstByLivreAndStatut(Livre livre, String statut);
    List<Exemplaire> findByStatutAndDateAjoutBeforeAndDateSuppressionIsNull(String statut, LocalDateTime date);

    @Query("SELECT e FROM Exemplaire e WHERE e.statut = 'disponible' AND e.dateAjout <= :date AND e.dateSuppression IS NULL")
    List<Exemplaire> findExemplairesDisponiblesAtDate(@Param("date") LocalDateTime date);
    List<Exemplaire> findByLivreId(Long idLivre);
    List<Exemplaire> findByLivreId(Integer idLivre);
}
