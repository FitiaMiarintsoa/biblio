package com.itu.bibliotheque.service.impl;

import com.itu.bibliotheque.model.Adherent;
import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Pret;
import com.itu.bibliotheque.repository.AdherentRepository;
import com.itu.bibliotheque.repository.ExemplaireRepository;
import com.itu.bibliotheque.repository.LivreRepository;
import com.itu.bibliotheque.repository.PretRepository;
import com.itu.bibliotheque.service.LivreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LivreServiceImpl implements LivreService {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private AdherentRepository adherentRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private PretRepository pretRepository;

    @Override
    public List<Livre> findAll() {
        return livreRepository.findAll();
    }

    @Override
    public Livre findById(Long id) {
        return livreRepository.findById(id).orElse(null);
    }

    @Override
    public Livre save(Livre livre) {
        return livreRepository.save(livre);
    }

    @Override
    public void deleteById(Long id) {
        livreRepository.deleteById(id);
    }
    @Override
    public List<Exemplaire> findExemplairesDisponiblesAtDate(LocalDateTime date) {
        return exemplaireRepository.findExemplairesDisponiblesAtDate(date);
    }

    // @Override
    // public List<Exemplaire> findExemplairesDisponiblesAtDate(LocalDate date) {
    //     return exemplaireRepository.findExemplairesDisponiblesAtDate(date);
    // }
}
