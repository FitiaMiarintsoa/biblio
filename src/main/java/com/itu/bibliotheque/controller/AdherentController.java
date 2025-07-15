package com.itu.bibliotheque.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import com.itu.bibliotheque.model.Abonnement;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Notification;
import com.itu.bibliotheque.model.Pret;
import com.itu.bibliotheque.model.Reservation;
import com.itu.bibliotheque.repository.AbonnementRepository;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ExemplaireRepository;
import com.itu.bibliotheque.repository.LivreRepository;
import com.itu.bibliotheque.repository.NotificationRepository;
import com.itu.bibliotheque.repository.ProfilRepository;
import com.itu.bibliotheque.repository.ReservationRepository;
import com.itu.bibliotheque.service.AdherentService;
import com.itu.bibliotheque.service.ReservationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/adherents")
public class AdherentController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private ProfilRepository profil;

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @GetMapping
    public String listeAdherents(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        return "bibliothecaire/listeAdherents";
    }

    @GetMapping("/fiche")
        public String showFiche(Model model) {
        return "bibliothecaire/fiche";
    }

    @GetMapping("/ajouter")
    public String showFormAjout(Model model) {
        model.addAttribute("profils", profil.findAll());
        return "bibliothecaire/ajout";
    }

    @PostMapping("/ajouter")
    public String enregistrerAdherent(
        @RequestParam(name = "nom") String nom,
        @RequestParam(name = "prenom") String prenom,
        @RequestParam(name = "email") String email,
        @RequestParam(name = "adresse") String adresse,
        @RequestParam(name = "dateNaissance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
        @RequestParam(name = "idProfil") Integer idProfil,
        @RequestParam(name = "souhaiteAbonnement", required = false) Boolean souhaiteAbonnement,
        Model model
    ) {
        try {
            adherentService.ajouterAdherent(nom, prenom, email, adresse, dateNaissance, idProfil, souhaiteAbonnement != null && souhaiteAbonnement);
            model.addAttribute("success", "Adhérent ajouté avec succès !");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("profils", profil.findAll());
        return "bibliothecaire/ajout";
    }


    @GetMapping("/abonner")
    public String showAbonnementForm(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        return "bibliothecaire/abonner";
    }

    @PostMapping("/abonner")
    public String enregistrerAbonnement(
        @RequestParam(name = "idAdherent") Integer idAdherent,
        @RequestParam(name = "dateDebut") String dateDebutStr,
        @RequestParam(name = "dateFin") String dateFinStr,
        Model model
    ) {
        try {
            Adherent adherent = adherentRepository.findById(idAdherent)
                .orElseThrow(() -> new RuntimeException("Adhérent introuvable"));

            Abonnement abonnement = new Abonnement();
            abonnement.setAdherent(adherent);
            abonnement.setDateDebut(LocalDate.parse(dateDebutStr));
            abonnement.setDateFin(LocalDate.parse(dateFinStr));

            abonnementRepository.save(abonnement);

            model.addAttribute("success", "Abonnement enregistré avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
        }

        model.addAttribute("adherents", adherentRepository.findAll());
        return "bibliothecaire/abonner";
    }
    @GetMapping("/emprunts")
    public String voirMesEmprunts(HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("userAdherent");
        if (adherent == null) {
            return "redirect:/adherent/login"; 
        }

        List<Pret> prets = adherentService.findPretsActifs(adherent); 
        model.addAttribute("prets", prets);
        model.addAttribute("user", adherent);
        return "adherent/emprunts";
    }

    @GetMapping("/notifications")
    public String voirNotifications(@RequestParam("id") Integer idAdherent, Model model) {
        Adherent adherent = adherentService.findById(idAdherent)
            .orElseThrow(() -> new RuntimeException("Adhérent introuvable"));

        adherentService.verifierEtNotifierRetard(adherent);

        List<Notification> notifications = adherentService.findNotificationsByAdherent(adherent);
        model.addAttribute("notifications", notifications);

        return "adherent/notification";
    }

    @GetMapping("/demander-reservation")
    public String showDemandeForm(HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("userAdherent");
        if (adherent == null) {
            return "redirect:/adherent/login";
        }

        model.addAttribute("exemplaires", exemplaireRepository.findAll());
        model.addAttribute("user", adherent);
        return "adherent/reservation";
    }

    @PostMapping("/demander-reservation")
    public String demanderReservation(
        @RequestParam("idExemplaire") Integer idExemplaire,
        @RequestParam("idAdherent") Integer idAdherent,
        @RequestParam("dateReservation") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
        Model model
    ) {
        try {
            Adherent adherent = adherentRepository.findById(idAdherent).orElseThrow(() -> new RuntimeException("Adhérent introuvable"));
            Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire).orElseThrow(() -> new RuntimeException("Exemplaire introuvable"));

            reservationService.demanderReservation(adherent, exemplaire, dateReservation);

            model.addAttribute("success", "Votre demande de réservation a été envoyée !");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
        }

        model.addAttribute("livres", livreRepository.findAll());
        model.addAttribute("user", adherentRepository.findById(idAdherent).orElse(null));
        return "adherent/reservation";
    }


    // @PostMapping("/demander-reservation")
    // public String demanderReservation(
    //     @RequestParam("idLivre") Integer idLivre,
    //     @RequestParam("idExemplaire") Integer idExemplaire,
    //     @RequestParam("idAdherent") Integer idAdherent,
    //     @RequestParam("dateReservation") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
    //     Model model
    // ) {
    //     try {
    //         Adherent adherent = adherentRepository.findById(idAdherent).orElseThrow();
    //         Livre livre = livreRepository.findById((long) idLivre).orElseThrow();
    //         Exemplaire exemplaire = exemplaireRepository.findById((int) idExemplaire).orElseThrow();

    //         reservationService.demanderReservation(adherent, livre, exemplaire, dateReservation);

    //         model.addAttribute("success", "Votre demande de réservation a été envoyée !");
    //     } catch (Exception e) {
    //         model.addAttribute("error", "Erreur : " + e.getMessage());
    //     }

    //     model.addAttribute("livres", livreRepository.findAll());
    //     return "redirect:/adherents/demander-reservation";
    // }

}
