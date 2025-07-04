package com.itu.bibliotheque.service.impl;

import com.itu.bibliotheque.model.Utilisateur;
import com.itu.bibliotheque.model.Adherent;
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
        if(userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            // comparer mot de passe (ici en clair, mais idéalement bcrypt)
            if(user.getMotDePasse().equals(password) && Boolean.TRUE.equals(user.getActif())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Adherent authenticateAdherent(String identifiant, String password) {
        Optional<Adherent> adhOpt = adherentRepository.findByIdentifiant(identifiant);
        if(adhOpt.isPresent()) {
            Adherent adh = adhOpt.get();
            // comparer mot de passe
            if(adh.getMotDePasse() != null && adh.getMotDePasse().equals(password) && !Boolean.TRUE.equals(adh.getEstBloque())) {
                return adh;
            }
        }
        return null;
    }
}
