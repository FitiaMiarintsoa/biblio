package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.ConfigurationQuota;
import com.itu.bibliotheque.model.Profil;
import com.itu.bibliotheque.repository.ConfigurationQuotaRepository;
import com.itu.bibliotheque.repository.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bibliothecaire/quota")
public class ConfigurationQuotaController {

    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;

    @Autowired
    private ProfilRepository profilRepository;

    @GetMapping
    public String listeQuota(Model model) {
        List<ConfigurationQuota> quotas = configurationQuotaRepository.findByDateSuppressionIsNull();
        model.addAttribute("quotas", quotas);
        return "bibliothecaire/quota/liste";
    }
    @GetMapping("/ajouter-profil")
    public String showAjouterProfilForm(Model model) {
        model.addAttribute("profil", new Profil());
        return "bibliothecaire/profil/edit";
    }

    @PostMapping("/ajouter-profil")
    public String saveProfil(@ModelAttribute Profil profil, Model model) {
        if (profil.getNom() == null || profil.getNom().trim().isEmpty()) {
            model.addAttribute("error", "Le nom du profil est requis.");
            return "bibliothecaire/profil/edit";
        }

        if (profilRepository.existsByNomIgnoreCase(profil.getNom())) {
            model.addAttribute("error", "Ce nom de profil existe déjà.");
            return "bibliothecaire/profil/edit";
        }

        profilRepository.save(profil);
        return "redirect:/bibliothecaire/quota";
    }



    @GetMapping("/edit")
    // public String editQuota(@RequestParam(required = false) Integer id, Model model) {
    //     ConfigurationQuota quota;
    //     if (id != null) {
    //         quota = configurationQuotaRepository.findById(id).orElse(new ConfigurationQuota());
    //     } else {
    //         quota = new ConfigurationQuota();
    //     }
    //     List<Profil> profils = profilRepository.findAll();
    //     model.addAttribute("quota", quota);
    //     model.addAttribute("profils", profils);
    //     return "bibliothecaire/quota/edit";
    // }

    public String editQuota(@RequestParam(name = "id", required = false) Integer id, Model model) {
        ConfigurationQuota quota;
        if (id != null) {
            quota = configurationQuotaRepository.findById(id).orElse(new ConfigurationQuota());
        } else {
            quota = new ConfigurationQuota();
        }
        List<Profil> profils = profilRepository.findAll();
        model.addAttribute("quota", quota);
        model.addAttribute("profils", profils);
        return "bibliothecaire/quota/edit";
    }


    // @PostMapping("/save")
    // public String saveQuota(@ModelAttribute("quota") ConfigurationQuota quota, Model model) {

    //     if (quota.getQuotaPret() == null || quota.getQuotaPret() < 0 ||
    //         quota.getQuotaPretPlace() == null || quota.getQuotaPretPlace() < 0 ||
    //         quota.getQuotaReservation() == null || quota.getQuotaReservation() < 0 ||
    //         quota.getQuotaProlongation() == null || quota.getQuotaProlongation() < 0 ||
    //         quota.getNbJour() == null || quota.getNbJour() <= 0) {
    //         model.addAttribute("error", "Tous les quotas doivent être remplis avec des valeurs valides.");
    //         model.addAttribute("quota", quota);
    //         model.addAttribute("profils", profilRepository.findAll());
    //         return "bibliothecaire/quota/edit";
    //     }
    //     boolean existe = configurationQuotaRepository.existsByProfilAndDateSuppressionIsNull(quota.getProfil());

    //     if (existe) {
    //         if (quota.getId() == null) {
    //             model.addAttribute("error", "Un quota existe déjà pour ce profil.");
    //             model.addAttribute("quota", quota);
    //             model.addAttribute("profils", profilRepository.findAll());
    //             return "bibliothecaire/quota/edit";
    //         } else {
    //             ConfigurationQuota quotaExistant = configurationQuotaRepository.findById(quota.getId())
    //                 .orElse(null);
    //             if (quotaExistant == null || !quotaExistant.getProfil().equals(quota.getProfil())) {
    //                 model.addAttribute("error", "Un quota existe déjà pour ce profil.");
    //                 model.addAttribute("quota", quota);
    //                 model.addAttribute("profils", profilRepository.findAll());
    //                 return "bibliothecaire/quota/edit";
    //             }
    //         }
    //     }

    //     configurationQuotaRepository.save(quota);
    //     return "redirect:/bibliothecaire/quota";
    // }

    @PostMapping("/save")
    public String saveQuota(@ModelAttribute("quota") ConfigurationQuota quota, Model model) {
        // Vérifie la validité de l'ID du profil
        if (quota.getProfil() == null || quota.getProfil().getId() == null) {
            model.addAttribute("error", "Le profil est requis.");
            model.addAttribute("quota", quota);
            model.addAttribute("profils", profilRepository.findAll());
            return "bibliothecaire/quota/edit";
        }

        // Recharge le profil complet à partir de son ID
        Profil profil = profilRepository.findById(quota.getProfil().getId()).orElse(null);
        if (profil == null) {
            model.addAttribute("error", "Profil introuvable.");
            model.addAttribute("quota", quota);
            model.addAttribute("profils", profilRepository.findAll());
            return "bibliothecaire/quota/edit";
        }

        quota.setProfil(profil); // Associe le bon objet profil

        // Validation des champs numériques
        if (quota.getQuotaPret() == null || quota.getQuotaPret() < 0 ||
            quota.getQuotaPretPlace() == null || quota.getQuotaPretPlace() < 0 ||
            quota.getQuotaReservation() == null || quota.getQuotaReservation() < 0 ||
            quota.getQuotaProlongation() == null || quota.getQuotaProlongation() < 0 ||
            quota.getNbJour() == null || quota.getNbJour() <= 0) {
            model.addAttribute("error", "Tous les quotas doivent être remplis avec des valeurs valides.");
            model.addAttribute("quota", quota);
            model.addAttribute("profils", profilRepository.findAll());
            return "bibliothecaire/quota/edit";
        }

        // Vérifie l'existence d'un quota pour ce profil
        boolean existe = configurationQuotaRepository.existsByProfilAndDateSuppressionIsNull(profil);

        if (existe) {
            if (quota.getId() == null) {
                model.addAttribute("error", "Un quota existe déjà pour ce profil.");
                model.addAttribute("quota", quota);
                model.addAttribute("profils", profilRepository.findAll());
                return "bibliothecaire/quota/edit";
            } else {
                ConfigurationQuota quotaExistant = configurationQuotaRepository.findById(quota.getId()).orElse(null);
                if (quotaExistant == null || !quotaExistant.getProfil().getId().equals(profil.getId())) {
                    model.addAttribute("error", "Un quota existe déjà pour ce profil.");
                    model.addAttribute("quota", quota);
                    model.addAttribute("profils", profilRepository.findAll());
                    return "bibliothecaire/quota/edit";
                }
            }
        }

        configurationQuotaRepository.save(quota);
        return "redirect:/bibliothecaire/quota";
    }




    @PostMapping("/delete")
    public String deleteQuota(@RequestParam Integer id) {
        configurationQuotaRepository.findById(id).ifPresent(quota -> {
            quota.setDateSuppression(java.time.LocalDateTime.now());
            configurationQuotaRepository.save(quota);
        });
        return "redirect:/bibliothecaire/quota";
    }
}
