package com.itu.bibliotheque.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuration_quota")
public class ConfigurationQuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_profil")
    private Profil profil;

    @Column(name = "quota_pret")
    private Integer quotaPret;

    @Column(name = "quota_prolongation")
    private Integer quotaProlongation;

    @Column(name = "nb_jour")
    private Integer nbJour;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @Column(name = "date_suppression")
    private LocalDateTime dateSuppression;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Profil getProfil() { return profil; }
    public void setProfil(Profil profil) { this.profil = profil; }

    public Integer getQuotaPret() { return quotaPret; }
    public void setQuotaPret(Integer quotaPret) { this.quotaPret = quotaPret; }

    public Integer getQuotaProlongation() { return quotaProlongation; }
    public void setQuotaProlongation(Integer quotaProlongation) { this.quotaProlongation = quotaProlongation; }

    public Integer getNbJour() { return nbJour; }
    public void setNbJour(Integer nbJour) { this.nbJour = nbJour; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    public LocalDateTime getDateSuppression() { return dateSuppression; }
    public void setDateSuppression(LocalDateTime dateSuppression) { this.dateSuppression = dateSuppression; }
}