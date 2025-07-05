package com.itu.bibliotheque.service.impl;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Personne;
import com.itu.bibliotheque.repository.UtilisateurRepository;
import com.itu.bibliotheque.repository.AdherentRepository;
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

    @Override
    public Utilisateur authenticateBibliothecaire(String username, String password) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (user.getMotDePasse().equals(password)
                && Boolean.TRUE.equals(user.getActif())
                && "bibliothecaire".equalsIgnoreCase(user.getRole())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Adherent authenticateAdherent(String username, String password) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (user.getMotDePasse().equals(password)
                && Boolean.TRUE.equals(user.getActif())
                && "adherent".equalsIgnoreCase(user.getRole())) {

                Optional<Adherent> adherentOpt = adherentRepository.findByPersonne(user.getPersonne());
                if (adherentOpt.isPresent()) {
                    Adherent adherent = adherentOpt.get();
                    if (!Boolean.TRUE.equals(adherent.getEstBloque())) {
                        return adherent;
                    }
                }
            }
        }
        return null;
    }
}
