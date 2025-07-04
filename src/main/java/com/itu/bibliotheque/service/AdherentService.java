package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AdherentService {

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private CategorieAdherentRepository categorieRepository;

    public void ajouterAdherent(String nom, String prenom, String email, String adresse, LocalDate dateNaissance, int idCategorie) {
        // Création de la personne
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setEmail(email);
        personne.setAdresse(adresse);
        personne.setDateNaissance(dateNaissance);
        personne.setDateAjout(LocalDateTime.now());

        personne = personneRepository.save(personne);

        CategorieAdherent categorie = categorieRepository.findById(idCategorie)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        String identifiant = nom.toLowerCase().replaceAll("\\s+", "") + new Random().nextInt(1000);
        String motDePasse = prenom.toLowerCase().replaceAll("\\s+", "") + new Random().nextInt(1000);

        Adherent adherent = new Adherent();
        adherent.setPersonne(personne);
        adherent.setIdentifiant(identifiant);
        adherent.setMotDePasse(motDePasse);
        adherent.setCategorieAdherent(categorie);
        adherent.setQuotaMax(5);
        adherent.setEstBloque(false);
        adherent.setDateAjout(LocalDateTime.now());

        adherentRepository.save(adherent);
    }
}
