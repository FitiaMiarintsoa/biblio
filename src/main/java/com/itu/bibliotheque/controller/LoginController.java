package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.BibliothecaireRepository;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Bibliothecaire;
import com.itu.bibliotheque.service.LoginService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private BibliothecaireRepository bibliothecaireRepository;

    @GetMapping("/login")
    public String login() {
        return "login";  // Une seule page de login générique
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        Utilisateur user = loginService.authenticate(username, password);
        if (user == null) {
            model.addAttribute("error", "Identifiants incorrects");
            return "login";
        }

        if ("bibliothecaire".equalsIgnoreCase(user.getRole())) {
            // Charge infos bibliothécaire
            Optional<Bibliothecaire> bibOpt = bibliothecaireRepository.findByPersonne(user.getPersonne());
            if (bibOpt.isPresent()) {
                session.setAttribute("user", user);
                session.setAttribute("role", "bibliothecaire");
                return "redirect:/bibliothecaire/home";
            } else {
                model.addAttribute("error", "Compte bibliothécaire non configuré");
                return "login";
            }
        } else if ("adherent".equalsIgnoreCase(user.getRole())) {
            Optional<Adherent> adhOpt = adherentRepository.findByPersonne(user.getPersonne());
            if (adhOpt.isPresent() && !Boolean.TRUE.equals(adhOpt.get().getEstBloque())) {
                session.setAttribute("user", adhOpt.get());
                session.setAttribute("role", "adherent");
                return "redirect:/adherent/home";
            } else {
                model.addAttribute("error", "Compte adhérent bloqué ou non trouvé");
                return "login";
            }
        } else {
            model.addAttribute("error", "Rôle utilisateur non reconnu");
            return "login";
        }
    }
}

