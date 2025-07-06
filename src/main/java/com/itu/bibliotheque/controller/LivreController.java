package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Pret;
import com.itu.bibliotheque.repository.ExemplaireRepository;
import com.itu.bibliotheque.repository.PretRepository;
import com.itu.bibliotheque.service.LivreService;
import com.itu.bibliotheque.service.PretService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private PretService pretService;

    @GetMapping("/livres")
    public String listLivres(Model model) {
        List<Livre> livres = livreService.findAll();
        model.addAttribute("livres", livres);
        return "livres";
    }
    
    // @GetMapping("/livres/disponibles")
    // public String livresDisponibles(
    //     @RequestParam(name = "date", required = false) String dateStr,
    //     Model model
    // ) {
    //     List<Exemplaire> exemplairesDisponibles = null;

    //     if (dateStr != null && !dateStr.isEmpty()) {
    //         try {
    //             LocalDate date = LocalDate.parse(dateStr);
    //             LocalDateTime dateTime = date.atStartOfDay();
    //             exemplairesDisponibles = livreService.findExemplairesDisponiblesAtDate(dateTime);
    //         } catch (Exception e) {
    //             model.addAttribute("error", "Date invalide.");
    //         }
    //     }

    //     model.addAttribute("exemplaires", exemplairesDisponibles);
    //     model.addAttribute("dateRecherche", dateStr);
    //     return "livres/disponibles";
    // }
}
