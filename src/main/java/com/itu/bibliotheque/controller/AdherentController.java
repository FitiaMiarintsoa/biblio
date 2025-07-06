package com.itu.bibliotheque.controller;

import java.time.LocalDate;

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
import com.itu.bibliotheque.repository.AbonnementRepository;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ProfilRepository;
import com.itu.bibliotheque.service.AdherentService;

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

    @GetMapping
    public String listeAdherents(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        return "bibliothecaire/listeAdherents";
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
        Model model
    ) {
        try {
            adherentService.ajouterAdherent(nom, prenom, email, adresse, dateNaissance, idProfil);
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
}
