package com.itu.bibliotheque.repository;

import com.itu.bibliotheque.model.ConfigurationQuota;
import com.itu.bibliotheque.model.Profil;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ConfigurationQuotaRepository extends JpaRepository<ConfigurationQuota, Integer> {
    Optional<ConfigurationQuota> findByProfil(Profil profil);
    Optional<ConfigurationQuota> findByProfilId(Integer idProfil);
    List<ConfigurationQuota> findByDateSuppressionIsNull();
    Optional<ConfigurationQuota> findByProfilAndDateSuppressionIsNull(Profil profil);
    boolean existsByProfilAndDateSuppressionIsNull(Profil profil);
}
