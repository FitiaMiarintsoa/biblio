package com.itu.bibliotheque.controller;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itu.bibliotheque.repository.CategorieAdherentRepository;
import com.itu.bibliotheque.service.AdherentService;



@RequestMapping("/adherents")
public class AdherentController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private CategorieAdherentRepository categorieRepository;

    @GetMapping("/ajouter")
    public String showFormAjout(Model model) {
        model.addAttribute("categories", categorieRepository.findAll());
        return "bibliothecaire/ajout";
    }

    @PostMapping("/ajouter")
    public String enregistrerAdherent(
        @RequestParam String nom,
        @RequestParam String prenom,
        @RequestParam String email,
        @RequestParam String adresse,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
        @RequestParam Integer idCategorie,
        Model model
    ) {
        try {
            adherentService.ajouterAdherent(nom, prenom, email, adresse, dateNaissance, idCategorie);
            model.addAttribute("success", "Adhérent ajouté avec succès !");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("categories", categorieRepository.findAll());
        return "bibliothecaire/ajout";
    }
}
