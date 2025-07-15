package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
public interface PretRepository extends JpaRepository<Pret, Integer> {
    long countByAdherentAndDateRetourReelleIsNull(Adherent adherent);
    Optional<Pret> findByAdherentAndExemplaireAndDateEmprunt(Adherent adherent, Exemplaire exemplaire, LocalDate dateEmprunt);
    List<Pret> findByDateRetourReelleIsNull();
    List<Pret> findByAdherentAndDateRetourReelleIsNull(Adherent adherent);
    List<Pret> findByDateRetourReelleIsNullAndDateRetourPrevueBefore(LocalDate date);
    List<Pret> findByAdherentAndDateRetourReelleIsNullAndDateRetourPrevueBefore(Adherent adherent, LocalDate date);
    List<Pret> findByAdherent(Adherent adherent);
    boolean existsByExemplaireAndDateEmpruntLessThanEqualAndDateRetourReelleGreaterThanEqual(Exemplaire exemplaire,LocalDate date1,LocalDate date2);
    List<Pret> findByAdherentIdAndDateRetourReelleIsNull(Integer idAdherent);
    long countByAdherentIdAndEstProlongeTrue(Integer idAdherent);

    int countByAdherentIdAndDateRetourReelleIsNull(Long id);
    int countByAdherentIdAndEstProlongeTrue(Long id);


}