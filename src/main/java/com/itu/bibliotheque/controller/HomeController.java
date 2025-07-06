package com.itu.bibliotheque.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Bibliothecaire;

@Controller
public class HomeController {
        
    @GetMapping("/")
    public String accueil() {
        return "index";
    }

    @GetMapping("/bibliothecaire/home")
    public String bibliothecaireHome(HttpSession session, Model model) {
        Bibliothecaire bibliothecaire = (Bibliothecaire) session.getAttribute("userBibliothecaire");
        if (bibliothecaire == null) {
            return "redirect:/bibliothecaire/login";
        }
        model.addAttribute("user", bibliothecaire);
        return "bibliothecaire/home";
    }

    @GetMapping("/adherent/home")
    public String adherentHome(HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("userAdherent");
        if (adherent == null) {
            return "redirect:/adherent/login";
        }
        model.addAttribute("user", adherent);
        return "adherent/home";
    }
}
