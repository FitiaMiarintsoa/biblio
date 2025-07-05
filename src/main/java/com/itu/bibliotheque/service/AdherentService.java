package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AdherentService {

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ProfilRepository profilRepository;

    public void ajouterAdherent(String nom, String prenom, String email, String adresse, LocalDate dateNaissance, int idProfil) {
        // Création et sauvegarde de la personne
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setEmail(email);
        personne.setAdresse(adresse);
        personne.setDateNaissance(dateNaissance);


        personne = personneRepository.save(personne);

        // Récupération du profil
        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        // Création de l’adhérent
        Adherent adherent = new Adherent();
        adherent.setPersonne(personne);
        adherent.setProfil(profil);
        adherent.setEstBloque(false);
        adherent.setDateAjout(LocalDateTime.now());

        adherentRepository.save(adherent);
    }
}
