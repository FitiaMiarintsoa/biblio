package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // PAGE LOGIN BIBLIOTHECAIRE
    @GetMapping("/bibliothecaire/login")
    public String loginBibliothecaire() {
        return "bibliothecaire/login";
    }

    @PostMapping("/bibliothecaire/login")
    public String doLoginBibliothecaire(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        Utilisateur user = loginService.authenticateBibliothecaire(username, password);
        if(user != null) {
            session.setAttribute("userBibliothecaire", user);
            return "redirect:/bibliothecaire/home";  
        } else {
            model.addAttribute("error", "Identifiants incorrects");
            return "bibliothecaire/login";
        }
    }

    // PAGE LOGIN ADHERENT
    @GetMapping("/adherent/login")
    public String loginAdherent() {
        return "adherent/login";
    }

   @PostMapping("/adherent/login")
  public String doLoginAdherent(@RequestParam("identifiant") String identifiant, @RequestParam("password") String password, HttpSession session, Model model) {
    Adherent adherent = loginService.authenticateAdherent(identifiant, password);
        if(adherent != null) {
            session.setAttribute("userAdherent", adherent);
            return "redirect:/adherent/home";  // page d'accueil après login
        } else {
            model.addAttribute("error", "Identifiants incorrects");
            return "adherent/login";
        }
    }
}
