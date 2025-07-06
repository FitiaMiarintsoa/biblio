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

    @Autowired
    private HistoriqueAdherentRepository historiqueAdherentRepository;

    @Autowired
    private TypeActionRepository typeActionRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;

    @Autowired
    private TypeSanctionRepository typeSanctionRepository;

    public String verifierEtEnregistrerPret(Adherent adherent, Exemplaire exemplaire, LocalDate dateEmprunt) {
        LocalDate aujourdHui = LocalDate.now();

        if (adherent == null ) {
            return "Adhérent introuvable.";
        }
        if (exemplaire == null) {
            return "Exemplaire introuvable.";
        }

        List<Sanction> sanctionsActives = sanctionRepository
                .findByAdherentAndEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                        adherent, aujourdHui, aujourdHui);

        if (!sanctionsActives.isEmpty()) {
            return "Cet adhérent est actuellement sanctionné et ne peut pas emprunter de livres.";
        }

        List<Pret> pretsEnCours = pretRepository.findByAdherentAndDateRetourReelleIsNull(adherent);
        for (Pret p : pretsEnCours) {
            if (p.getDateRetourPrevue().isBefore(aujourdHui)) {
                return "L’adhérent a un livre non rendu dont la date de retour est dépassée.";
            }
        }

        if (adherent.getPersonne().getDateNaissance() != null && exemplaire.getLivre().getRestrictionAge() != null) {
            int age = Period.between(adherent.getPersonne().getDateNaissance(), aujourdHui).getYears();
            int restriction = exemplaire.getLivre().getRestrictionAge();
            if (age < restriction) {
                return "L’adhérent n’a pas l’âge requis pour emprunter ce livre.";
            }
        }

        ConfigurationQuota quota = configurationQuotaRepository.findByProfil(adherent.getProfil())
                .orElseThrow(() -> new RuntimeException("Configuration de quota introuvable pour ce profil."));

        long nbPretsEnCours = pretRepository.countByAdherentAndDateRetourReelleIsNull(adherent);
        if (nbPretsEnCours >= quota.getQuotaPret()) {
            return "Cet adhérent a atteint le nombre maximal de livres empruntés.";
        }

        // Calcule automatique de la date de retour prévue
        LocalDate dateRetourPrevue = dateEmprunt.plusDays(quota.getNbJour());

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

        enregistrerHistorique(adherent, "emprunt", "L'adhérent a emprunté l’exemplaire " + exemplaire.getId());

        return null;
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
            Adherent adherent = pret.getAdherent();
            TypeSanction typeSanction = typeSanctionRepository.findByNom("Retard de retour")
                    .orElseThrow(() -> new RuntimeException("Type de sanction 'Retard de retour' introuvable"));

            Sanction sanction = new Sanction();
            sanction.setAdherent(adherent);
            sanction.setTypeSanction(typeSanction);
            sanction.setDescription("Retard de retour du livre n°" + ex.getId());
            sanction.setDateDebut(LocalDate.now());
            sanction.setDateFin(LocalDate.now().plusDays(typeSanction.getPenaliteJour()));
            sanction.setEstActive(true);
            sanction.setDateAjout(LocalDateTime.now());
            sanctionRepository.save(sanction);

            Notification notification = new Notification();
            notification.setAdherent(adherent);
            notification.setMessage("Vous avez reçu une sanction pour retard de retour du livre. "
                    + "Sanction active du " + sanction.getDateDebut() + " au " + sanction.getDateFin() + ".");
            notification.setEstLu(false);
            notification.setDateNotification(LocalDateTime.now());
            notificationRepository.save(notification);
        }

        enregistrerHistorique(pret.getAdherent(), "retour", "L'adhérent a rendu l’exemplaire " + pret.getExemplaire().getId());

        return null;
    }


    private void enregistrerHistorique(Adherent adherent, String nomAction, String commentaire) {
        TypeAction action = typeActionRepository.findByNom(nomAction)
                .orElseThrow(() -> new RuntimeException("Type d'action introuvable : " + nomAction));

        HistoriqueAdherent historique = new HistoriqueAdherent();
        historique.setAdherent(adherent);
        historique.setTypeAction(action);
        historique.setCommentaire(commentaire);
        historique.setDateAction(LocalDateTime.now());

        historiqueAdherentRepository.save(historique);
    }
}
