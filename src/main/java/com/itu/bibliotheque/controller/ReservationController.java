package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.ConfigurationQuota;
import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Reservation;
import com.itu.bibliotheque.service.ReservationService;

import jakarta.servlet.http.HttpServletResponse;

import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ConfigurationQuotaRepository;
import com.itu.bibliotheque.repository.ExemplaireRepository;
import com.itu.bibliotheque.repository.LivreRepository;
import com.itu.bibliotheque.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
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
        model.addAttribute("exemplaires", exemplaireRepository.findAll());
        return "bibliothecaire/reserver";
    }

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    // @GetMapping("/exemplaires-par-livre")
    // @ResponseBody
    // public List<Exemplaire> getExemplairesParLivre(@RequestParam("idLivre") Long idLivre) {
    //     return exemplaireRepository.findByLivreId(idLivre);
    // }

    // @GetMapping("/exemplaires-par-livre")
    // public void getExemplairesParLivre(@RequestParam("idLivre") Long idLivre, HttpServletResponse response) {
    //     try {
    //         List<Exemplaire> exemplaires = exemplaireRepository.findByLivreId(idLivre);

    //         response.setContentType("application/json");
    //         PrintWriter out = response.getWriter();
    //         out.print("[");
    //         for (int i = 0; i < exemplaires.size(); i++) {
    //             Exemplaire e = exemplaires.get(i);
    //             out.print("{\"id\":" + e.getId() + ",\"statut\":\"" + e.getStatut() + "\"}");
    //             if (i < exemplaires.size() - 1) out.print(",");
    //         }
    //         out.print("]");
    //         out.flush();
    //     } catch (Exception e) {
    //         response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //     }
    // }



    @PostMapping("/reserver")
    // public String enregistrerReservation(
    //     @RequestParam("idAdherent") Integer idAdherent,
    //     @RequestParam("idLivre") Integer idLivre,
    //     @RequestParam("dateReservation") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
    //     Model model
    // ) {
    //     Adherent adherent = adherentRepository.findById(idAdherent).orElseThrow();
    //     Livre livre = livreRepository.findById((long) idLivre).orElseThrow();

    //     try {
    //         reservationService.reserver(adherent, livre, dateReservation);
    //         model.addAttribute("success", "Réservation ajoutée !");
    //     } catch (IllegalStateException e) {
    //         model.addAttribute("error", e.getMessage());
    //     } catch (Exception e) {
    //         model.addAttribute("error", "Une erreur s’est produite lors de la réservation.");
    //     }

    //     model.addAttribute("livres", livreRepository.findAll());
    //     model.addAttribute("user", adherent);
    //     return "bibliothecaire/reserver"; 
    // }

    public String enregistrerReservation(
        @RequestParam("idAdherent") Integer idAdherent,
        // @RequestParam("idLivre") Integer idLivre,
        @RequestParam("idExemplaire") Integer idExemplaire,
        @RequestParam("dateReservation") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
        Model model
    ) {
        Adherent adherent = adherentRepository.findById(idAdherent).orElseThrow();
        // Livre livre = livreRepository.findById((long) idLivre).orElseThrow();
        Exemplaire exemplaire = exemplaireRepository.findById((int) idExemplaire).orElseThrow();

        try {
            reservationService.reserver(adherent, exemplaire, dateReservation);
            model.addAttribute("success", "Réservation confirmée !");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
        }

        model.addAttribute("livres", livreRepository.findAll());
        model.addAttribute("adherents", adherentRepository.findAll());
        return "bibliothecaire/reserver";
    }



    @GetMapping("/valider-reservations")
    public String voirDemandes(Model model) {
        List<Reservation> demandes = reservationRepository.findByStatut("en_attente");
        model.addAttribute("reservations", demandes);
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