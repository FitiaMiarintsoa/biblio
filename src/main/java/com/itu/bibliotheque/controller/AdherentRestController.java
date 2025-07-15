package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/adherents")
public class AdherentRestController {

    @Autowired private AdherentRepository adherentRepository;
    @Autowired private AbonnementRepository abonnementRepository;
    @Autowired private SanctionRepository sanctionRepository;
    @Autowired private HistoriqueAdherentRepository historiqueRepository;
    @Autowired private ConfigurationQuotaRepository configurationQuotaRepository;
    @Autowired private PretRepository pretRepository;
    @Autowired private ReservationRepository reservationRepository;

    @GetMapping("/{id}")
public Map<String, Object> getFicheAdherent(@PathVariable("id") Long id) {
    Map<String, Object> fiche = new HashMap<>();

    Optional<Adherent> optAdherent = adherentRepository.findById(id.intValue());
    if (optAdherent.isEmpty()) {
        return Map.of("error", "Adhérent introuvable");
    }

    Adherent adherent = optAdherent.get();
    fiche.put("id", adherent.getId());
    fiche.put("nom", adherent.getPersonne().getNom());
    fiche.put("prenom", adherent.getPersonne().getPrenom());
    fiche.put("email", adherent.getPersonne().getEmail());
    fiche.put("adresse", adherent.getPersonne().getAdresse());
    fiche.put("profil", adherent.getProfil().getNom());
    fiche.put("bloque", adherent.getEstBloque());

    fiche.put("abonnement", abonnementRepository.findTopByAdherentIdOrderByDateFinDesc(id));
    fiche.put("sanctions", sanctionRepository.findByAdherentIdAndEstActiveTrue(id));
    fiche.put("historique", historiqueRepository.findByAdherentIdOrderByDateActionDesc(id));

    Optional<ConfigurationQuota> optQuota = configurationQuotaRepository.findByProfil(adherent.getProfil());

    Map<String, Integer> quotasRestants = new HashMap<>();

    if (optQuota.isPresent()) {
        ConfigurationQuota quota = optQuota.get();

        int nbPretsActifs = pretRepository.countByAdherentIdAndDateRetourReelleIsNull(id);
        int nbReservationsActives = reservationRepository.countByAdherentIdAndStatut(id, "en_attente");
        int nbProlongations = pretRepository.countByAdherentIdAndEstProlongeTrue(id);

        quotasRestants.put("pret", quota.getQuotaPret() - nbPretsActifs);
        quotasRestants.put("reservation", quota.getQuotaReservation() - nbReservationsActives);
        quotasRestants.put("prolongation", quota.getQuotaProlongation() - nbProlongations);
        quotasRestants.put("pret_place", quota.getQuotaPretPlace());
    }

    fiche.put("quotasRestants", quotasRestants);
    return fiche;
}
}
