package com.itu.bibliotheque.service;

import java.util.Optional;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Bibliothecaire;
import com.itu.bibliotheque.model.Utilisateur;

public interface LoginService {
    Utilisateur authenticate(String username, String password);
        Optional<Adherent> getAdherent(Utilisateur user);
    Optional<Bibliothecaire> getBibliothecaire(Utilisateur user);
}
