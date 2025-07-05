package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Reservation;
import com.itu.bibliotheque.service.ReservationService;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.LivreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @GetMapping("/nouvelle")
    public String formReservation(Model model) {
        model.addAttribute("livres", livreRepository.findAll());
        model.addAttribute("adherents", adherentRepository.findAll());
        return "reservation/form";
    }

    // @PostMapping("/nouvelle")
    // public String reserverLivre(@RequestParam("idLivre") int idLivre,
    //                             @RequestParam("idAdherent") int idAdherent,
    //                             Model model) {
    //     String result = reservationService.reserverLivre(idLivre, idAdherent);
    //     model.addAttribute("message", result);
    //     model.addAttribute("livres", livreRepository.findAll());
    //     model.addAttribute("adherents", adherentRepository.findAll());
    //     return "reservation/form";
    // }
}