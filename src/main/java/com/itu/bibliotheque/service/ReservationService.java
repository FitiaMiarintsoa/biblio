package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    /**
     * Crée une réservation pour un livre emprunté.
     */
    public String reserverLivre(int idAdherent, int idLivre) {
        Optional<Adherent> optionalAdherent = adherentRepository.findById(idAdherent);
        Optional<Livre> optionalLivre = livreRepository.findById((long) idLivre);
        if (optionalLivre.isEmpty()) {
            return "Livre introuvable.";
        }

        if (optionalAdherent.isEmpty() || optionalLivre.isEmpty()) {
            return "Adhérent ou livre introuvable.";
        }

        Adherent adherent = optionalAdherent.get();
        Livre livre = optionalLivre.get();

        // Vérifier si le livre est actuellement indisponible
        Optional<Exemplaire> exemplaireDispo = exemplaireRepository.findFirstByIdLivreAndStatut(livre.getId(), "disponible");
        Optional<Exemplaire> exemplaireEmprunte = exemplaireRepository.findFirstByIdLivreAndStatut(livre.getId(), "emprunte");

        if (exemplaireEmprunte.isEmpty()) {
            return "Ce livre n'est pas emprunté, donc la réservation n'est pas nécessaire.";
        }

        // Vérifier si une réservation existe déjà
        boolean dejaReserve = reservationRepository.existsByAdherentAndLivreAndStatut(adherent, livre, "en_attente");
        if (dejaReserve) {
            return "Une réservation en attente existe déjà pour cet adhérent et ce livre.";
        }

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setAdherent(adherent);
        reservation.setLivre(livre);
        reservation.setDateReservation(LocalDate.now());
        reservation.setExpireLe(LocalDate.now().plusDays(5));
        reservation.setStatut("en_attente");
        reservation.setDateAjout(LocalDateTime.now());

        reservationRepository.save(reservation);

        return "Réservation effectuée avec succès. Elle sera traitée dès que le livre sera disponible.";
    }

    /**
     * Le bibliothécaire confirme la réservation → prêt automatique
     */
    public String confirmerReservation(int idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable."));

        if (!"en_attente".equals(reservation.getStatut())) {
            return "La réservation n'est pas en attente.";
        }

        Livre livre = reservation.getLivre();
        Adherent adherent = reservation.getAdherent();

        // Chercher un exemplaire disponible
        Optional<Exemplaire> exemplaireDispo = exemplaireRepository.findFirstByIdLivreAndStatut(livre.getId(), "disponible");

        if (exemplaireDispo.isEmpty()) {
            return "Aucun exemplaire n'est disponible pour confirmer la réservation.";
        }

        Exemplaire exemplaire = exemplaireDispo.get();

        // Créer un prêt
        Pret pret = new Pret();
        pret.setAdherent(adherent);
        pret.setExemplaire(exemplaire);
        pret.setDateEmprunt(LocalDate.now());
        pret.setDateRetourPrevue(LocalDate.now().plusWeeks(2));
        pret.setDateAjout(LocalDateTime.now());
        pret.setEstProlonge(false);

        pretRepository.save(pret);

        // Mettre à jour l'exemplaire
        exemplaire.setStatut("emprunte");
        exemplaireRepository.save(exemplaire);

        // Mettre à jour la réservation
        reservation.setStatut("confirmee");
        reservationRepository.save(reservation);

        return "Réservation confirmée et prêt effectué.";
    }
}
