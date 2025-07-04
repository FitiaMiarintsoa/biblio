package com.itu.bibliotheque.service;

// import com.itu.bibliotheque.model.Exemplaire;
// import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.*;
import com.itu.bibliotheque.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
// import java.util.List;
import java.util.UUID;
public class AdherentService {
    private PersonneRepository personneRepository;
    private AdherentRepository adherentRepository;
    private CategorieAdherentRepository categorieRepository;

    public void ajouterAdherent(String nom, String prenom, String email, String adresse, LocalDate dateNaissance, int idCategorie) {
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

        Adherent adherent = new Adherent();
        adherent.setPersonne(personne);
        adherent.setIdentifiant(UUID.randomUUID().toString().substring(0, 8)); // identifiant aléatoire
        adherent.setCategorieAdherent(categorie);
        adherent.setQuotaMax(5);
        adherent.setEstBloque(false);
        adherent.setDateAjout(LocalDateTime.now());

        adherentRepository.save(adherent);
    }
}
