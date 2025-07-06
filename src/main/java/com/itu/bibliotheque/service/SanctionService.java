package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Sanction;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.SanctionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SanctionService {

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    // ⏰ S’exécute tous les jours à 02:00 du matin
@Scheduled(cron = "0 0 2 * * *")
public void verifierSanctionsEtMettreAJourAdherents() {
    LocalDate aujourdHui = LocalDate.now();

    List<Adherent> tousLesAdherents = adherentRepository.findAll();
    for (Adherent adherent : tousLesAdherents) {
        // Désactiver les sanctions terminées
        List<Sanction> sanctions = sanctionRepository.findByAdherentAndEstActiveTrue(adherent);
        for (Sanction s : sanctions) {
            if (s.getDateFin().isBefore(aujourdHui)) {
                s.setEstActive(false);
                sanctionRepository.save(s);
            }
        }

        // Vérifier s’il reste des sanctions actives
        boolean aSanctionActive = sanctionRepository
            .existsByAdherentAndEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                    adherent, aujourdHui, aujourdHui);

        adherent.setEstBloque(aSanctionActive);
        adherentRepository.save(adherent);
    }
}

}
