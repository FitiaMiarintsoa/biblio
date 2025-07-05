package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Bibliothecaire;
import com.itu.bibliotheque.service.LoginService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // Page de login adhérent
    @GetMapping("/adherent/login")
    public String adherentLogin() {
        return "adherent/login";
    }

    @PostMapping("/adherent/login")
    public String doAdherentLogin(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpSession session,
            Model model) {

        Utilisateur user = loginService.authenticate(username, password);
        if (user == null) {
            model.addAttribute("error", "Identifiants incorrects");
            return "adherent/login";
        }

        Optional<Adherent> adhOpt = loginService.getAdherent(user);
        if (adhOpt.isPresent()) {
            session.setAttribute("userAdherent", adhOpt.get());
            session.setAttribute("role", "adherent");
            return "redirect:/adherent/home";
        } else {
            model.addAttribute("error", "Adhérent bloqué ou non trouvé");
            return "adherent/login";
        }
    }

    // Page de login bibliothécaire
    @GetMapping("/bibliothecaire/login")
    public String bibliothecaireLogin() {
        return "bibliothecaire/login";
    }

    @PostMapping("/bibliothecaire/login")
    public String doBibliothecaireLogin(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpSession session,
            Model model) {

        Utilisateur user = loginService.authenticate(username, password);
        if (user == null) {
            model.addAttribute("error", "Identifiants incorrects");
            return "bibliothecaire/login";
        }

        Optional<Bibliothecaire> bibOpt = loginService.getBibliothecaire(user);
        if (bibOpt.isPresent()) {
            session.setAttribute("userBibliothecaire", bibOpt.get());
            session.setAttribute("role", "bibliothecaire");
            return "redirect:/bibliothecaire/home";
        } else {
            model.addAttribute("error", "Bibliothécaire non trouvé");
            return "bibliothecaire/login";
        }
    }
}
