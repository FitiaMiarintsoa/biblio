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

    // @Override
    // public boolean rendreLivre(String identifiantAdherent, String isbnLivre, LocalDate dateEmprunt, LocalDate dateRetourReelle) {
    //     // Convertir identifiantAdherent en Long (supposé être l'id réel)
    //     Long adherentId;
    //     try {
    //         adherentId = Long.parseLong(identifiantAdherent);
    //     } catch (NumberFormatException e) {
    //         throw new RuntimeException("Identifiant de l'adhérent invalide : " + identifiantAdherent);
    //     }

    //     Adherent adherent = adherentRepository.findById(adherentId)
    //         .orElseThrow(() -> new RuntimeException("Adhérent non trouvé"));

    //     Livre livre = livreRepository.findByIsbn(isbnLivre)
    //         .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

    //     Exemplaire exemplaire = exemplaireRepository.findFirstByIdLivreAndStatut(livre.getId(), "emprunte")
    //         .orElseThrow(() -> new RuntimeException("Aucun exemplaire emprunté trouvé"));

    //     Pret pret = pretRepository.findByAdherentAndExemplaireAndDateEmprunt(adherent, exemplaire, dateEmprunt)
    //         .orElseThrow(() -> new RuntimeException("Prêt introuvable pour ces informations"));

    //     pret.setDateRetourReelle(dateRetourReelle);
    //     pretRepository.save(pret);

    //     exemplaire.setStatut("disponible");
    //     exemplaireRepository.save(exemplaire);

    //     return true;
    // }

    @Override
    public List<Exemplaire> findExemplairesDisponiblesAtDate(LocalDateTime date) {
        return exemplaireRepository.findByStatutAndDateAjoutBeforeAndDateSuppressionIsNull("disponible", date);
    }
}
