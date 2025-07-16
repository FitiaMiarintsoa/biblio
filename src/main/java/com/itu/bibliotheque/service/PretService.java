package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @Autowired
    private ConfigurationJourOuvreRepository configurationJourOuvreRepository;


    public String verifierEtEnregistrerPret(Adherent adherent, Exemplaire exemplaire, LocalDate dateEmprunt) {
        LocalDate aujourdHui = LocalDate.now();

        if (adherent == null) return "Adhérent introuvable.";
        if (exemplaire == null) return "Exemplaire introuvable.";

        // Refuser si date d'emprunt = jour férié ou dimanche
        if (estJourNonOuvre(dateEmprunt)) {
            return "La date d'emprunt ne peut pas être un dimanche ou un jour férié.";
        }

        List<Sanction> sanctionsActives = sanctionRepository
                .findByAdherentAndEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(adherent, dateEmprunt, dateEmprunt);

        if (!sanctionsActives.isEmpty()) {
            return "Cet adhérent est sanctionné à la date d'emprunt et ne peut pas emprunter de livres.";
        }

        boolean estAbonne = abonnementRepository
                .existsByAdherentAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(adherent, dateEmprunt, dateEmprunt);

        if (!estAbonne) {
            return "L'adhérent n'est pas abonné à la date d'emprunt.";
        }

        boolean dejaEmprunteCeJour = pretRepository
                .existsByExemplaireAndDateEmpruntLessThanEqualAndDateRetourReelleGreaterThanEqual(exemplaire, dateEmprunt, dateEmprunt);

        if (dejaEmprunteCeJour) {
            return "Ce livre n’est pas disponible à la date choisie.";
        }

        List<Pret> pretsEnCours = pretRepository.findByAdherentAndDateRetourReelleIsNull(adherent);
        for (Pret p : pretsEnCours) {
            if (p.getDateRetourPrevue().isBefore(dateEmprunt)) {
                return "L’adhérent a un livre non rendu en retard à la date d’emprunt.";
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

        // Calcule la date de retour prévue puis l'ajuste si c’est un jour non ouvré
        LocalDate dateRetourPrevue = dateEmprunt.plusDays(quota.getNbJour());
        dateRetourPrevue = ajusterAuJourOuvreSelonConfig(dateRetourPrevue);

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

    public boolean estJourNonOuvre(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY || jourFerieRepository.existsByDateFerie(date);
    }

    public LocalDate ajusterAuProchainJourOuvre(LocalDate date) {
        while (estJourNonOuvre(date)) {
            date = date.plusDays(1);
        }
        return date;
    }
    public LocalDate ajusterAuJourOuvreSelonConfig(LocalDate date) {
        ConfigurationJourOuvre config = configurationJourOuvreRepository.findTopByOrderByIdDesc();
        if (config == null || config.getDirectionDecalage().equalsIgnoreCase("apres")) {
            while (estJourNonOuvre(date)) {
                date = date.plusDays(1);
            }
        } else if (config.getDirectionDecalage().equalsIgnoreCase("avant")) {
            while (estJourNonOuvre(date)) {
                date = date.minusDays(1);
            }
        }
        return date;
    }

    // public String rendreLivre(Integer pretId, LocalDate dateRetourReelle) {
    //     Pret pret = pretRepository.findById(pretId)
    //             .orElseThrow(() -> new RuntimeException("Prêt introuvable"));

    //     if (pret.getDateRetourReelle() != null) {
    //         return "Ce livre a déjà été rendu.";
    //     }

    //     pret.setDateRetourReelle(dateRetourReelle);
    //     pretRepository.save(pret);

    //     Exemplaire ex = pret.getExemplaire();
    //     ex.setStatut("disponible");
    //     exemplaireRepository.save(ex);

    //     if (dateRetourReelle.isAfter(pret.getDateRetourPrevue())) {
    //         Adherent adherent = pret.getAdherent();
    //         TypeSanction typeSanction = typeSanctionRepository.findByNom("Retard de retour")
    //                 .orElseThrow(() -> new RuntimeException("Type de sanction 'Retard de retour' introuvable"));

    //         Sanction sanction = new Sanction();
    //         sanction.setAdherent(adherent);
    //         sanction.setTypeSanction(typeSanction);
    //         sanction.setDescription("Retard de retour du livre n°" + ex.getId());
    //         sanction.setDateDebut(dateRetourReelle);
    //         sanction.setDateFin(dateRetourReelle.plusDays(typeSanction.getPenaliteJour()));
    //         sanction.setEstActive(true);
    //         sanction.setDateAjout(LocalDateTime.now());
    //         sanctionRepository.save(sanction);

    //         Notification notification = new Notification();
    //         notification.setAdherent(adherent);
    //         notification.setMessage("Vous avez reçu une sanction pour retard de retour du livre. "
    //                 + "Sanction active du " + sanction.getDateDebut() + " au " + sanction.getDateFin() + ".");
    //         notification.setEstLu(false);
    //         notification.setDateNotification(LocalDateTime.now());
    //         notificationRepository.save(notification);
    //     }

    //     enregistrerHistorique(pret.getAdherent(), "retour", "L'adhérent a rendu l’exemplaire " + pret.getExemplaire().getId());
    //     return null;
    // }

    public String rendreLivre(Integer pretId, LocalDate dateRetourReelle) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt introuvable"));

        if (pret.getDateRetourReelle() != null) {
            return "Ce livre a déjà été rendu.";
        }

        if (estJourNonOuvre(dateRetourReelle)) {
            return "La date de retour ne peut pas être un dimanche ou un jour férié.";
        }
        pret.setDateRetourReelle(dateRetourReelle);
        pretRepository.save(pret);

        Exemplaire exemplaire = pret.getExemplaire();
        exemplaire.setStatut("disponible");
        exemplaireRepository.save(exemplaire);

        if (dateRetourReelle.isAfter(pret.getDateRetourPrevue())) {
            Adherent adherent = pret.getAdherent();
            int idProfil = adherent.getProfil().getId();
        int idTypeSanction;

        switch (idProfil) {
            case 1:
                idTypeSanction = 1; 
                break;
            case 2:
                idTypeSanction = 2; 
                break;
            case 3:
                idTypeSanction = 3; 
                break;
            default:
                throw new RuntimeException("Aucune règle de sanction définie pour ce profil.");
        }


            TypeSanction typeSanction = typeSanctionRepository.findById(idTypeSanction)
                    .orElseThrow(() -> new RuntimeException("Type de sanction ID " + idTypeSanction + " introuvable."));

            Sanction sanction = new Sanction();
            sanction.setAdherent(adherent);
            sanction.setTypeSanction(typeSanction);
            sanction.setDescription("Sanction automatique pour retard de retour du livre n°" + exemplaire.getId());
            sanction.setDateDebut(dateRetourReelle);
            sanction.setDateFin(dateRetourReelle.plusDays(typeSanction.getPenaliteJour()));
            sanction.setEstActive(true);
            sanction.setDateAjout(LocalDateTime.now());
            sanctionRepository.save(sanction);

            Notification notification = new Notification();
            notification.setAdherent(adherent);
            notification.setMessage("Sanction active du " + sanction.getDateDebut()
                    + " au " + sanction.getDateFin() + " pour le livre n°" + exemplaire.getId());
            notification.setEstLu(false);
            notification.setDateNotification(LocalDateTime.now());
            notificationRepository.save(notification);
        }

        enregistrerHistorique(pret.getAdherent(), "retour", "L'adhérent a rendu l’exemplaire " + exemplaire.getId());
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
