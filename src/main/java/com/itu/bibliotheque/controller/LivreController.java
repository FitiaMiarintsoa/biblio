package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Pret;
import com.itu.bibliotheque.repository.ExemplaireRepository;
import com.itu.bibliotheque.repository.PretRepository;
import com.itu.bibliotheque.service.LivreService;

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
        model.addAttribute("pretSelectionne", new Pret());
        return "livres/rendre";
    }

    @PostMapping("/livres/rendre")
    public String rendreLivre(
        @RequestParam("pretId") Integer pretId,
        @RequestParam("dateRetourReelle") String dateRetourStr,
        Model model
    ) {
        try {
            LocalDate dateRetourReelle = LocalDate.parse(dateRetourStr);
            Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt introuvable"));

            if (pret.getDateRetourReelle() != null) {
                throw new RuntimeException("Ce livre a déjà été rendu.");
            }

            pret.setDateRetourReelle(dateRetourReelle);
            pretRepository.save(pret);

            Exemplaire ex = pret.getExemplaire();
            ex.setStatut("disponible");
            exemplaireRepository.save(ex);

            if (dateRetourReelle.isAfter(pret.getDateRetourPrevue())) {
                
                System.out.println("⚠️ Livre rendu en retard !");
            }

            model.addAttribute("message", "Livre rendu avec succès.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("prets", pretRepository.findByDateRetourReelleIsNull());
        model.addAttribute("pretSelectionne", new Pret());
        return "livres/rendre";
    }

    @GetMapping("/livres/disponibles")
    public String livresDisponibles(
        @RequestParam(name = "date", required = false) String dateStr,
        Model model
    ) {
        List<Exemplaire> exemplairesDisponibles = null;

        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(dateStr);
                LocalDateTime dateTime = date.atStartOfDay();
                exemplairesDisponibles = exemplaireRepository.findByStatutAndDateAjoutBeforeAndDateSuppressionIsNull("disponible", dateTime);
            } catch (Exception e) {
                model.addAttribute("error", "Date invalide.");
            }
        }

        model.addAttribute("exemplaires", exemplairesDisponibles);
        model.addAttribute("dateRecherche", dateStr);
        return "livres/disponibles";
    }
}
