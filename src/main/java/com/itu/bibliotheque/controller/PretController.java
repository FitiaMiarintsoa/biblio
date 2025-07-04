package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import com.itu.bibliotheque.repository.PretRepository;
import com.itu.bibliotheque.repository.SanctionRepository;
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

    @Autowired
    private SanctionRepository sanctionRepository;

    @GetMapping("/nouveau-pret")
    public String showForm(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
        return "bibliothecaire/emprunt";
    }

    @Autowired
    private PretService pretService;

    @PostMapping("/save")
    public String enregistrerPret(@RequestParam("idAdherent") int idAdherent,
                                @RequestParam("idExemplaire") int idExemplaire,
                                @RequestParam("dateEmprunt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEmprunt,
                                @RequestParam("dateRetourPrevue") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRetourPrevue,
                                Model model) {

        Adherent adherent = adherentRepository.findById(idAdherent).orElse(null);
        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire).orElse(null);

        String erreur = pretService.verifierEtEnregistrerPret(adherent, exemplaire, dateEmprunt, dateRetourPrevue);

        if (erreur != null) {
            model.addAttribute("error", erreur);
            model.addAttribute("adherents", adherentRepository.findAll());
            model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
            return "bibliothecaire/emprunt";
        }

        model.addAttribute("success", "Le prêt a été enregistré avec succès !");
        return "redirect:/bibliothecaire/emprunt";
    }


}
