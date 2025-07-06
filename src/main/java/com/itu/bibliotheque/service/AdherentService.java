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

    public void ajouterAdherent(String nom, String prenom, String email, String adresse, LocalDate dateNaissance, int idProfil) {
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setEmail(email);
        personne.setAdresse(adresse);
        personne.setDateNaissance(dateNaissance);


        personne = personneRepository.save(personne);

        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        Adherent adherent = new Adherent();
        adherent.setPersonne(personne);
        adherent.setProfil(profil);
        adherent.setEstBloque(false);
        adherent.setDateAjout(LocalDateTime.now());

        adherentRepository.save(adherent);
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
}
