DROP DATABASE IF EXISTS bibliotheque;
CREATE DATABASE bibliotheque;
\c bibliotheque;

    CREATE TABLE Profil (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(50) UNIQUE NOT NULL,
        description TEXT,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP
    );

    CREATE TABLE personne (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(100) NOT NULL,
        prenom VARCHAR(100) NOT NULL,
        email VARCHAR(150) UNIQUE NOT NULL,
        adresse VARCHAR(255),
        date_naissance DATE
    );

    CREATE TABLE adherent (
        id SERIAL PRIMARY KEY,
        id_profil INT NOT NULL,
        id_personne INT NOT NULL,
        est_bloque BOOLEAN DEFAULT FALSE,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_adherent_personne FOREIGN KEY (id_personne) REFERENCES personne(id) ON DELETE CASCADE,
        CONSTRAINT fk_adherent_profil  FOREIGN KEY (id_profil) REFERENCES Profil(id) ON DELETE RESTRICT
    );

    CREATE TABLE bibliothecaire (
        id SERIAL PRIMARY KEY,
        id_personne INT NOT NULL,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_bibliothecaire_personne FOREIGN KEY (id_personne) REFERENCES personne(id) ON DELETE CASCADE
    );

    CREATE TABLE role (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(100) UNIQUE NOT NULL,
        description TEXT
    );

    CREATE TABLE utilisateur (
        id SERIAL PRIMARY KEY,
        id_personne INT NOT NULL,
        username VARCHAR(100) UNIQUE NOT NULL,
        mot_de_passe VARCHAR(255) NOT NULL,
        actif BOOLEAN DEFAULT TRUE,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        FOREIGN KEY (id_personne) REFERENCES personne(id)
    );

    CREATE TABLE utilisateur_role (
        id_utilisateur INT,
        id_role INT,
        PRIMARY KEY (id_utilisateur, id_role),
        FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE,
        FOREIGN KEY (id_role) REFERENCES role(id) ON DELETE CASCADE
    );

    CREATE TABLE livre (
        id SERIAL PRIMARY KEY,
        titre VARCHAR(255) NOT NULL,
        auteur VARCHAR(150),
        genre VARCHAR(100),
        isbn VARCHAR(50) UNIQUE NOT NULL,
        restriction_age INT,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP
    );

    CREATE TABLE exemplaire (
        id SERIAL PRIMARY KEY,
        id_livre INT NOT NULL,
        statut VARCHAR(50) DEFAULT 'disponible' CHECK (statut IN ('disponible', 'emprunte', 'perdu')),
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_exemplaire_livre FOREIGN KEY (id_livre) REFERENCES livre(id) ON DELETE CASCADE
    );

    CREATE TABLE pret (
        id SERIAL PRIMARY KEY,
        id_adherent INT NOT NULL,
        id_exemplaire INT NOT NULL,
        date_emprunt DATE NOT NULL,
        date_retour_prevue DATE NOT NULL,
        date_retour_reelle DATE,
        est_prolonge BOOLEAN DEFAULT FALSE,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_pret_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE RESTRICT,
        CONSTRAINT fk_pret_exemplaire FOREIGN KEY (id_exemplaire) REFERENCES exemplaire(id) ON DELETE RESTRICT
    );

    CREATE TABLE reservation (
        id SERIAL PRIMARY KEY,
        id_adherent INT NOT NULL,
        id_exemplaire INT,
        date_reservation DATE DEFAULT CURRENT_DATE,
        expire_le DATE,
        statut VARCHAR(50) DEFAULT 'en_attente' CHECK (statut IN ('en_attente', 'confirmee', 'expiree', 'annulee','refusee')),
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_reservation_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE,
        CONSTRAINT fk_reservation_exemplaire FOREIGN KEY (id_exemplaire) REFERENCES exemplaire(id) ON DELETE SET NULL
    );

    CREATE TABLE type_action (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(100) UNIQUE NOT NULL,
        description TEXT,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP
    );

    CREATE TABLE historique_adherent (
        id SERIAL PRIMARY KEY,
        id_adherent INT NOT NULL,
        id_type_action INT NOT NULL,
        commentaire TEXT,
        date_action TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_historique_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE,
        CONSTRAINT fk_historique_type_action FOREIGN KEY (id_type_action) REFERENCES type_action(id) ON DELETE RESTRICT
    );

    CREATE TABLE configuration_quota (
        id SERIAL PRIMARY KEY,
        id_profil INT NOT NULL,
        quota_pret INT, 
        quota_pret_place INT,
        quota_reservation INT,
        quota_prolongation INT,
        nb_jour INT,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_configuration_profil  FOREIGN KEY (id_profil) REFERENCES Profil(id) ON DELETE CASCADE
    );

    CREATE TABLE notification (
        id SERIAL PRIMARY KEY,
        id_adherent INT NOT NULL,
        message TEXT NOT NULL,
        est_lu BOOLEAN DEFAULT FALSE,
        date_notification TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_notification_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE
    );

    CREATE TABLE type_sanction (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(100) UNIQUE NOT NULL,
        description TEXT,
        penalite_jour INT DEFAULT 0
    );

    CREATE TABLE sanction (
        id SERIAL PRIMARY KEY,
        id_adherent INT NOT NULL,
        id_type_sanction INT NOT NULL,
        description TEXT,
        date_debut DATE NOT NULL,
        date_fin DATE,
        est_active BOOLEAN DEFAULT TRUE,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_sanction_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE,
        CONSTRAINT fk_sanction_type FOREIGN KEY (id_type_sanction) REFERENCES type_sanction(id) ON DELETE RESTRICT
    );

    CREATE TABLE abonnement (
        id SERIAL PRIMARY KEY,
        id_adherent INT NOT NULL,
        date_debut DATE NOT NULL,
        date_fin DATE NOT NULL,
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_suppression TIMESTAMP,
        CONSTRAINT fk_abonnement_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE
    );

    CREATE TABLE jour_ferie (
        id SERIAL PRIMARY KEY,
        date_ferie DATE UNIQUE NOT NULL,
        libelle VARCHAR(100) DEFAULT 'Jour férié'
    );

    CREATE TABLE configuration_jour_ouvre (
        id SERIAL PRIMARY KEY,
        direction_decalage VARCHAR(10) NOT NULL CHECK (direction_decalage IN ('avant', 'apres')),
        date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        date_modification TIMESTAMP
    );

