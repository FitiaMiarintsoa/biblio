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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

import com.itu.bibliotheque.repository.PretRepository;
import com.itu.bibliotheque.repository.SanctionRepository;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ConfigurationQuotaRepository;
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
    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;
    
    // @GetMapping("/prets")
    // public String gestionPrets() {
    //     return "bibliothecaire/prets"; 
    // }

    @GetMapping("/prets")
    public String gestionPrets(
            @RequestParam(value = "statut", required = false) String statut,
            @RequestParam(value = "idAdherent", required = false) Integer idAdherent,
            Model model) {

        List<Pret> prets;
        LocalDate aujourdHui = LocalDate.now();

        if (statut != null && idAdherent != null) {
            Adherent adherent = adherentRepository.findById(idAdherent).orElse(null);
            if (adherent == null) {
                model.addAttribute("error", "Adhérent non trouvé.");
                prets = List.of();
            } else {
                if ("en_cours".equals(statut)) {
                    prets = pretRepository.findByAdherentAndDateRetourReelleIsNull(adherent);
                } else if ("en_retard".equals(statut)) {
                    prets = pretRepository.findByAdherentAndDateRetourReelleIsNullAndDateRetourPrevueBefore(adherent, aujourdHui);
                } else {
                    prets = pretRepository.findByAdherent(adherent);
                }
            }
        } else if (statut != null) {
            if ("en_cours".equals(statut)) {
                prets = pretRepository.findByDateRetourReelleIsNull();
            } else if ("en_retard".equals(statut)) {
                prets = pretRepository.findByDateRetourReelleIsNullAndDateRetourPrevueBefore(aujourdHui);
            } else {
                prets = pretRepository.findAll();
            }
        } else if (idAdherent != null) {
            Adherent adherent = adherentRepository.findById(idAdherent).orElse(null);
            if (adherent == null) {
                model.addAttribute("error", "Adhérent non trouvé.");
                prets = List.of();
            } else {
                prets = pretRepository.findByAdherent(adherent);
            }
        } else {
            prets = pretRepository.findAll();
        }

        model.addAttribute("prets", prets);
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("statutSelectionne", statut);
        model.addAttribute("idAdherentSelectionne", idAdherent);

        return "bibliothecaire/prets";
    }

    @GetMapping("/nouveau-pret")
    public String showForm(Model model) {
        model.addAttribute("adherents", adherentRepository.findAll());
        model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
        return "bibliothecaire/emprunt";
    }

    @Autowired
    private PretService pretService;

    @PostMapping("/save")
    public String enregistrerPret(
            @RequestParam("idAdherent") int idAdherent,
            @RequestParam("idExemplaire") int idExemplaire,
            @RequestParam("dateEmprunt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEmprunt,
            Model model) {

        Adherent adherent = adherentRepository.findById(idAdherent).orElse(null);
        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire).orElse(null);

        String erreur = pretService.verifierEtEnregistrerPret(adherent, exemplaire, dateEmprunt);

        if (erreur != null) {
            model.addAttribute("error", erreur);
            model.addAttribute("adherents", adherentRepository.findAll());
            model.addAttribute("exemplaires", exemplaireRepository.findByStatut("disponible"));
            return "bibliothecaire/emprunt";
        }

        model.addAttribute("success", "Le prêt a été enregistré avec succès !");
        return "redirect:/bibliothecaire/nouveau-pret";
    }

    @GetMapping("/rendre")
    public String showFormRendreLivre(Model model) {
        List<Pret> pretsEnCours = pretRepository.findByDateRetourReelleIsNull();
        model.addAttribute("pretsEnCours", pretsEnCours);
        return "bibliothecaire/rendre";
    }

    @PostMapping("/rendre")
    public String enregistrerRetour(
            @RequestParam("pretId") Integer pretId,
            @RequestParam("dateRetour") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRetour,
            Model model) {
        try {
            String erreur = pretService.rendreLivre(pretId, dateRetour);
            if (erreur != null) {
                model.addAttribute("error", erreur);
            } else {
                model.addAttribute("success", "Livre rendu avec succès !");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
        }
        model.addAttribute("pretsEnCours", pretRepository.findByDateRetourReelleIsNull());
        return "bibliothecaire/rendre";
    }

    @GetMapping("/retards")
    public String afficherPretsEnRetard(Model model) {
        LocalDate aujourdHui = LocalDate.now();
        List<Pret> pretsEnRetard = pretRepository.findByDateRetourReelleIsNullAndDateRetourPrevueBefore(aujourdHui);
        model.addAttribute("pretsEnRetard", pretsEnRetard);
        return "bibliothecaire/prets_retards";
    }

    @GetMapping("/prets_encours")
    public String pretsEncours(
            @RequestParam(required = false) Integer idAdherent,
            Model model) {

        List<Adherent> adherents = adherentRepository.findAll();
        model.addAttribute("adherents", adherents);
        model.addAttribute("idAdherent", idAdherent);

        List<Pret> prets;
        if (idAdherent != null) {
            prets = pretRepository.findByAdherentIdAndDateRetourReelleIsNull(idAdherent);
        } else {
            prets = pretRepository.findByDateRetourReelleIsNull();
        }
        model.addAttribute("prets", prets);

        return "bibliothecaire/prets_encours";
    }

    @PostMapping("/prets_encours/prolonger")
    public String prolongerPret(
            @RequestParam("idPret") Integer idPret,
            @RequestParam(value = "idAdherent", required = false) Integer idAdherent,
            RedirectAttributes redirectAttributes) {

        try {
            Pret pret = pretRepository.findById(idPret)
                    .orElseThrow(() -> new RuntimeException("Prêt introuvable"));
            Adherent adherent = pret.getAdherent();

            ConfigurationQuota quota = configurationQuotaRepository.findByProfilAndDateSuppressionIsNull(adherent.getProfil())
                    .orElseThrow(() -> new RuntimeException("Configuration quota introuvable pour ce profil"));

            long nbProlongations = pretRepository.countByAdherentIdAndEstProlongeTrue(adherent.getId());

            if (pret.getEstProlonge()) {
                redirectAttributes.addFlashAttribute("error", "Ce prêt a déjà été prolongé.");
            } else if (nbProlongations >= quota.getQuotaProlongation()) {
                redirectAttributes.addFlashAttribute("error", "Quota de prolongation dépassé pour cet adhérent.");
            } else {
                pret.setDateRetourPrevue(pret.getDateRetourPrevue().plusDays(quota.getNbJour()));
                pret.setEstProlonge(true);
                pretRepository.save(pret);
                redirectAttributes.addFlashAttribute("success", "Prêt prolongé avec succès.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la prolongation : " + e.getMessage());
        }

        if (idAdherent != null) {
            return "redirect:/bibliothecaire/prets_encours?idAdherent=" + idAdherent;
        }
        return "redirect:/bibliothecaire/prets_encours";
    }

}
