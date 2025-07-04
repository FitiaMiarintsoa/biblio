package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class PretService {

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    public String verifierEtEnregistrerPret(Adherent adherent, Exemplaire exemplaire, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        LocalDate aujourdHui = LocalDate.now();

        if (adherent == null || exemplaire == null) {
            return "Adhérent ou exemplaire introuvable.";
        }

        List<Sanction> sanctionsActives = sanctionRepository
            .findByAdherentAndEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                adherent, aujourdHui, aujourdHui);

        if (!sanctionsActives.isEmpty()) {
            return "Cet adhérent est actuellement sanctionné et ne peut pas emprunter de livres.";
        }

        if (!"disponible".equalsIgnoreCase(exemplaire.getStatut())) {
            return "Ce livre n'est pas disponible pour l'emprunt.";
        }

        if (adherent.getDateNaissance() != null && exemplaire.getLivre().getRestrictionAge() != null) {
            int age = Period.between(adherent.getDateNaissance(), aujourdHui).getYears();
            int restriction = exemplaire.getLivre().getRestrictionAge();
            if (age < restriction) {
                return "L’adhérent n’a pas l’âge requis pour emprunter ce livre.";
            }
        }

        long nbPretsEnCours = pretRepository.countByAdherentAndDateRetourReelleIsNull(adherent);
        if (nbPretsEnCours >= adherent.getQuotaMax()) {
            return "Cet adhérent a atteint le nombre maximal de livres empruntés.";
        }

        // Enregistrement du prêt
        Pret pret = new Pret();
        pret.setAdherent(adherent);
        pret.setExemplaire(exemplaire);
        pret.setDateEmprunt(dateEmprunt);
        pret.setDateRetourPrevue(dateRetourPrevue);
        pret.setDateAjout(LocalDateTime.now());
        pret.setEstProlonge(false);
        pretRepository.save(pret);

        exemplaire.setStatut("emprunte");
        exemplaireRepository.save(exemplaire);

        return null; // Tout est OK
    }

    public String rendreLivre(Integer pretId, LocalDate dateRetourReelle) {
        Pret pret = pretRepository.findById(pretId)
            .orElseThrow(() -> new RuntimeException("Prêt introuvable"));

        if (pret.getDateRetourReelle() != null) {
            return "Ce livre a déjà été rendu.";
        }

        pret.setDateRetourReelle(dateRetourReelle);
        pretRepository.save(pret);

        Exemplaire ex = pret.getExemplaire();
        ex.setStatut("disponible");
        exemplaireRepository.save(ex);

        if (dateRetourReelle.isAfter(pret.getDateRetourPrevue())) {
            // Ici, tu peux déclencher la création d'une notification ou d'une sanction
            System.out.println("⚠️ Livre rendu en retard !");
        }

        return null; // succès
    }
}
