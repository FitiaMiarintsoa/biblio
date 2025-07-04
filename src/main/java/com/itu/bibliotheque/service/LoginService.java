package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.model.Adherent;

public interface LoginService {
    Utilisateur authenticateBibliothecaire(String username, String password);
    Adherent authenticateAdherent(String identifiant, String password);
}
