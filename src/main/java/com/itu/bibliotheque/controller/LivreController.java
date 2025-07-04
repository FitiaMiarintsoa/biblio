package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Pret;
import com.itu.bibliotheque.service.LivreService;
import com.itu.bibliotheque.repository.PretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping("/livres")
    public String listLivres(Model model) {
        List<Livre> livres = livreService.findAll();
        model.addAttribute("livres", livres);
        return "livres";
    }
    @GetMapping("/livres/rendre")
    public String showFormRendre(Model model) {
        List<Pret> pretsEnCours = pretRepository.findByDateRetourReelleIsNull();
        model.addAttribute("prets", pretsEnCours);
        model.addAttribute("pretSelectionne", new Pret()); // pour le rendu
        return "livres/rendre";
    }

    @PostMapping("/livres/rendre")
    public String rendreLivre(
        @RequestParam String identifiantAdherent,
        @RequestParam String isbnLivre,
        @RequestParam String dateEmprunt,
        @RequestParam String dateRetourReelle,
        Model model
    ) {
        try {
            LocalDate dateEmpruntParsed = LocalDate.parse(dateEmprunt);
            LocalDate dateRetourParsed = LocalDate.parse(dateRetourReelle);

            boolean success = livreService.rendreLivre(identifiantAdherent, isbnLivre, dateEmpruntParsed, dateRetourParsed);

            if (success) {
                model.addAttribute("message", "Livre rendu avec succès.");
            } else {
                model.addAttribute("error", "Échec du rendu. Prêt introuvable.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
        }

        return "livres/rendre";
    }

    
}
