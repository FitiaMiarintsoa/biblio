package com.itu.bibliotheque.service;

import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.model.Livre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LivreService {
    List<Livre> findAll();
    Livre findById(Long id);
    Livre save(Livre livre);
    void deleteById(Long id);
    // boolean rendreLivre(String identifiantAdherent, String isbnLivre, LocalDate dateEmprunt, LocalDate dateRetourReelle);
    List<Exemplaire> findExemplairesDisponiblesAtDate(LocalDateTime date);
    // List<Exemplaire> findExemplairesDisponiblesAtDate(LocalDate date);

}
