package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.ConfigurationQuota;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Reservation;
import com.itu.bibliotheque.service.ReservationService;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ConfigurationQuotaRepository;
import com.itu.bibliotheque.repository.LivreRepository;
import com.itu.bibliotheque.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;

    @GetMapping("/reserver")
    public String showReservationForm(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("livres", livreRepository.findAll());
        return "bibliothecaire/reserver";
    }

    @PostMapping("/reserver")
    public String enregistrerReservation(
        @RequestParam("idAdherent") Integer idAdherent,
        @RequestParam("idLivre") Integer idLivre,
        @RequestParam("dateReservation") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
        Model model
    ) {
        Adherent adherent = adherentRepository.findById(idAdherent).orElseThrow();
        Livre livre = livreRepository.findById((long) idLivre).orElseThrow();

        reservationService.reserver(adherent, livre, dateReservation);

        model.addAttribute("success", "Réservation ajoutée !");
        return "redirect:/reservations/reserver";
    }

    @GetMapping("/valider-reservations")
    public String voirDemandes(Model model) {
        List<Reservation> demandes = reservationRepository.findByStatut("en_attente");
        model.addAttribute("demandes", demandes);
        return "bibliothecaire/reservation";
    }

    @PostMapping("/valider-reservation")
    public String validerReservation(@RequestParam("idReservation") Integer id) {
        Reservation r = reservationRepository.findById(id).orElseThrow();
        r.setStatut("confirmee");
        reservationRepository.save(r);
        return "redirect:/reservations/valider-reservations";
    }

    @PostMapping("/refuser-reservation")
    public String refuserReservation(@RequestParam("idReservation") Integer id) {
        Reservation r = reservationRepository.findById(id).orElseThrow();
        r.setStatut("refusee");
        reservationRepository.save(r);
        return "redirect:/reservations/valider-reservations";
    }
}