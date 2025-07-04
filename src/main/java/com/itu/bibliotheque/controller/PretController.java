package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import com.itu.bibliotheque.model.Pret;
import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.repository.PretRepository;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ExemplaireRepository;
import java.time.LocalDateTime;
import java.time.Period;


import java.util.List;
@Controller
@RequestMapping("/bibliothecaire")
public class PretController {

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @GetMapping("/nouveau-pret")
    public String showForm(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
        return "bibliothecaire/emprunt";
    }

    // @PostMapping("/save")
@PostMapping("/save")
public String enregistrerPret(@RequestParam("idAdherent") int idAdherent,
                              @RequestParam("idExemplaire") int idExemplaire,
                              @RequestParam("dateEmprunt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEmprunt,
                              @RequestParam("dateRetourPrevue") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRetourPrevue,
                              Model model) {
 

    // 1. Récupération des entités
    Adherent adherent = adherentRepository.findById(idAdherent).orElse(null);
    Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire).orElse(null);

    if (adherent == null || exemplaire == null) {
        model.addAttribute("error", "Adhérent ou exemplaire introuvable.");
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
        return "bibliothecaire/emprunt";
    }

    // RG1 - Livre doit être disponible
    if (!"disponible".equalsIgnoreCase(exemplaire.getStatut())) {
        model.addAttribute("error", "Ce livre n'est pas disponible pour l'emprunt.");
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
        return "bibliothecaire/emprunt";
    }

    // RG2 - Âge requis
    if (adherent.getDateNaissance() != null && exemplaire.getLivre().getRestrictionAge() != null) {
        int age = Period.between(adherent.getDateNaissance(), LocalDate.now()).getYears();
        int restriction = exemplaire.getLivre().getRestrictionAge();
        if (age < restriction) {
            model.addAttribute("error", "L’adhérent n’a pas l’âge requis pour emprunter ce livre.");
            model.addAttribute("adherents", adherentRepository.findAll());
            model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
            return "bibliothecaire/emprunt";
        }
    }

    // RG3 - Quota d’emprunt non dépassé
    long nbPretsEnCours = pretRepository.countByAdherentAndDateRetourReelleIsNull(adherent);
    if (nbPretsEnCours >= adherent.getQuotaMax()) {
        model.addAttribute("error", "Cet adhérent a atteint le nombre maximal de livres empruntés.");
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
        return "bibliothecaire/emprunt";
    }

    // RG4 - Vérification que c'est bien un adhérent (déjà fait implicitement)

    // ✅ Enregistrement du prêt
    Pret pret = new Pret();
    pret.setAdherent(adherent);
    pret.setExemplaire(exemplaire);
    pret.setDateEmprunt(dateEmprunt);
    pret.setDateRetourPrevue(dateRetourPrevue);
    pret.setDateAjout(LocalDateTime.now());
    pret.setEstProlonge(false);
    pretRepository.save(pret);

    // 🔁 Mise à jour du statut de l’exemplaire
    exemplaire.setStatut("emprunte");
    exemplaireRepository.save(exemplaire);

    model.addAttribute("success", "Le prêt a été enregistré avec succès !");
    return "redirect:/bibliothecaire/emprunt";
}


}
