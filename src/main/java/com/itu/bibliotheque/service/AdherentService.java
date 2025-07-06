package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdherentService {

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private NotificationRepository notificationRepository; 

    @Autowired
    private AbonnementRepository abonnementRepository; 

    public void ajouterAdherent(String nom, String prenom, String email, String adresse, LocalDate dateNaissance, Integer idProfil, boolean souhaiteAbonnement) {
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setEmail(email);
        personne.setAdresse(adresse);
        personne.setDateNaissance(dateNaissance);
        personneRepository.save(personne);

        Profil profil = profilRepository.findById(idProfil)
            .orElseThrow(() -> new RuntimeException("Profil introuvable"));

        Adherent adherent = new Adherent();
        adherent.setPersonne(personne);
        adherent.setProfil(profil);
        adherent.setEstBloque(false);
        adherent.setDateAjout(LocalDateTime.now());
        adherentRepository.save(adherent);

        if (souhaiteAbonnement) {
            Abonnement abonnement = new Abonnement();
            abonnement.setAdherent(adherent);
            abonnement.setDateDebut(LocalDate.now());
            abonnement.setDateFin(LocalDate.now().plusMonths(1));
            abonnementRepository.save(abonnement);
        }
    }


    public void verifierEtNotifierRetard(Adherent adherent) {
        LocalDate aujourdHui = LocalDate.now();
        List<Pret> pretsEnRetard = pretRepository
            .findByAdherentAndDateRetourReelleIsNullAndDateRetourPrevueBefore(adherent, aujourdHui);

        for (Pret pret : pretsEnRetard) {
            String message = "Vous avez un livre en retard : exemplaire n°" + pret.getExemplaire().getId() +
                            ", à rendre avant le " + pret.getDateRetourPrevue() + ".";

            boolean dejaNotifie = notificationRepository.existsByAdherentAndMessageAndEstLuFalse(adherent, message);
            if (!dejaNotifie) {
                Notification notif = new Notification();
                notif.setAdherent(adherent);
                notif.setMessage(message);
                notif.setEstLu(false);
                notif.setDateNotification(LocalDateTime.now());
                notificationRepository.save(notif);
            }
        }
    }

    public Optional<Adherent> findById(Integer id) {
        return adherentRepository.findById(id);
    }

    public List<Notification> findNotificationsByAdherent(Adherent adherent) {
        return notificationRepository.findByAdherentOrderByDateNotificationDesc(adherent);
    }

    public List<Pret> findPretsActifs(Adherent adherent) {
        return pretRepository.findByAdherentAndDateRetourReelleIsNull(adherent);
    }
}
