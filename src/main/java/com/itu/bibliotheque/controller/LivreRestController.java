package com.itu.bibliotheque.controller;

import com.itu.bibliotheque.model.Livre;
import com.itu.bibliotheque.model.Exemplaire;
import com.itu.bibliotheque.repository.LivreRepository;
import com.itu.bibliotheque.repository.ExemplaireRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class LivreRestController {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @GetMapping("/livres-exemplaires")
    public List<Map<String, Object>> getLivresEtExemplaires() {
        List<Livre> livres = livreRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Livre livre : livres) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("livre", livre);
            entry.put("exemplaires", exemplaireRepository.findByLivreId(livre.getId()));
            result.add(entry);
        }

        return result;
    }
}

