package com.itu.bibliotheque.service.impl;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Bibliothecaire;
import com.itu.bibliotheque.repository.UtilisateurRepository;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.BibliothecaireRepository;
import com.itu.bibliotheque.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private BibliothecaireRepository bibliothecaireRepository;

    @Override
    public Utilisateur authenticate(String username, String password) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (user.getMotDePasse().equals(password)
                && Boolean.TRUE.equals(user.getActif())) {
                return user;
            }
        }
        return null;
    }

    public Optional<Adherent> getAdherent(Utilisateur user) {
        return adherentRepository.findByPersonne(user.getPersonne())
                .filter(adh -> !Boolean.TRUE.equals(adh.getEstBloque()));
    }

    public Optional<Bibliothecaire> getBibliothecaire(Utilisateur user) {
        return bibliothecaireRepository.findByPersonne(user.getPersonne());
    }
}
