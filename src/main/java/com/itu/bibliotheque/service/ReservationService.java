package com.itu.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.ConfigurationQuota;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Reservation;
import com.itu.bibliotheque.repository.ConfigurationQuotaRepository;
import com.itu.bibliotheque.repository.ReservationRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;

    /**
     * Crée une réservation en calculant la date d'expiration selon le profil de l'adhérent.
     * @param adherent l'adhérent qui réserve
     * @param livre le livre à réserver
     * @param dateReservation date de réservation choisie
     * @return la réservation créée
     */
    public Reservation reserver(Adherent adherent, Livre livre, LocalDate dateReservation) {
        int nbJour = 0;
        int quotaReservation = Integer.MAX_VALUE; 

        // Récupérer le quota selon le profil
        if (adherent.getProfil() != null) {
            Optional<ConfigurationQuota> quotaOpt = configurationQuotaRepository.findByProfilId(adherent.getProfil().getId());
            if (quotaOpt.isPresent()) {
                ConfigurationQuota quota = quotaOpt.get();
                nbJour = quota.getNbJour();
                quotaReservation = quota.getQuotaReservation(); 
            }
        }

        int nbReservationsActives = reservationRepository.countByAdherentAndStatutIn(
            adherent,
            List.of("en_attente", "valide")
        );

        if (nbReservationsActives >= quotaReservation) {
            throw new IllegalStateException("Quota de réservations atteint.");
        }

        // Créer la réservation confirmée
        Reservation reservation = new Reservation();
        reservation.setAdherent(adherent);
        reservation.setLivre(livre);
        reservation.setDateReservation(dateReservation);
        reservation.setExpireLe(dateReservation.plusDays(nbJour));
        reservation.setStatut("confirmee");

        return reservationRepository.save(reservation);
    }


    public Reservation demanderReservation(Adherent adherent, Livre livre, LocalDate dateReservation) {
        int nbJour = 0;
        int quotaReservation = Integer.MAX_VALUE;

        if (adherent.getProfil() != null) {
            Optional<ConfigurationQuota> quotaOpt = configurationQuotaRepository.findByProfilId(adherent.getProfil().getId());
            if (quotaOpt.isPresent()) {
                ConfigurationQuota quota = quotaOpt.get();
                nbJour = quota.getNbJour();
                quotaReservation = quota.getQuotaReservation(); 
            }
        }

        int nbReservationsActives = reservationRepository.countByAdherentAndStatutIn(
            adherent,
            List.of("en_attente", "valide") 
        );

        if (nbReservationsActives >= quotaReservation) {
            throw new IllegalStateException("Quota de réservations atteint.");
        }

        Reservation reservation = new Reservation();
        reservation.setAdherent(adherent);
        reservation.setLivre(livre);
        reservation.setDateReservation(dateReservation);
        reservation.setExpireLe(dateReservation.plusDays(nbJour));
        reservation.setStatut("en_attente");

        return reservationRepository.save(reservation);
    }

}
