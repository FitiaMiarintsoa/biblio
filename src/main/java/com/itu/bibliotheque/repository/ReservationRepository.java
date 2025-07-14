package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.Reservation;
// import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Exemplaire;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByExemplaireAndStatutOrderByDateReservationAsc(Exemplaire exemplaire, String statut);

    // // Vérifie si un adhérent a déjà réservé un livre
    // boolean existsByAdherentAndLivreAndStatut(Adherent adherent, Livre livre, String statut);

    // // Trouve toutes les réservations en attente pour un livre
    // List<Reservation> findByLivreAndStatutOrderByDateReservationAsc(Livre livre, String statut);

    // Trouve toutes les réservations d’un adhérent
    List<Reservation> findByAdherent(Adherent adherent);

    // Pour que le bibliothécaire gère les réservations en attente
    List<Reservation> findByStatut(String statut);

    int countByAdherentAndStatutIn(Adherent adherent, List<String> statuts);

}
