package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.ConfigurationJourOuvre;
import com.itu.bibliotheque.repository.ConfigurationJourOuvreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/configuration")
public class ConfigurationJourOuvreController {

    @Autowired
    private ConfigurationJourOuvreRepository configurationJourOuvreRepository;

    @GetMapping("/jour-ouvre")
    public String afficherForm(Model model) {
        ConfigurationJourOuvre config = configurationJourOuvreRepository.findTopByOrderByIdDesc();
        if (config == null) {
            config = new ConfigurationJourOuvre();
            config.setDirectionDecalage("apres");
        }
        model.addAttribute("config", config);
        return "bibliothecaire/jour_ouvre";
    }

    @PostMapping("/jour-ouvre")
    public String enregistrerConfig(@RequestParam("directionDecalage") String direction) {
        ConfigurationJourOuvre config = new ConfigurationJourOuvre();
        config.setDirectionDecalage(direction);
        config.setDateAjout(LocalDateTime.now());
        config.setDateModification(LocalDateTime.now());
        configurationJourOuvreRepository.save(config);
        return "redirect:/configuration/jour-ouvre?success";
    }
}
