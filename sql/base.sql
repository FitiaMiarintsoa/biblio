
DROP DATABASE IF EXISTS bibliotheque;
CREATE DATABASE bibliotheque;
\c bibliotheque;

-- ===============================
-- TABLE: categorie_adherent
-- ===============================
CREATE TABLE categorie_adherent (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP
);

-- ===============================
-- TABLE: personne
-- ===============================
CREATE TABLE personne (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    adresse VARCHAR(255),
    date_naissance DATE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP
);

-- ===============================
-- TABLE: adherent  
-- ===============================
CREATE TABLE adherent (
    id INT PRIMARY KEY,
    identifiant VARCHAR(50) UNIQUE NOT NULL,
    id_categorie_adherent INT NOT NULL,
    quota_max INT DEFAULT 5 CHECK (quota_max >= 1),
    est_bloque BOOLEAN DEFAULT FALSE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_adherent_personne FOREIGN KEY (id) REFERENCES personne(id) ON DELETE CASCADE,
    CONSTRAINT fk_adherent_categorie FOREIGN KEY (id_categorie_adherent) REFERENCES categorie_adherent(id) ON DELETE RESTRICT
);

-- ===============================
-- TABLE: bibliothecaire
-- ===============================
CREATE TABLE bibliothecaire (
    id INT PRIMARY KEY,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_bibliothecaire_personne FOREIGN KEY (id) REFERENCES personne(id) ON DELETE CASCADE
);

-- ===============================
-- TABLE: livre
-- ===============================
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

-- ===============================
-- TABLE: exemplaire
-- ===============================
CREATE TABLE exemplaire (
    id SERIAL PRIMARY KEY,
    id_livre INT NOT NULL,
    statut VARCHAR(50) DEFAULT 'disponible' CHECK (statut IN ('disponible', 'emprunte', 'perdu')),
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_exemplaire_livre FOREIGN KEY (id_livre) REFERENCES livre(id) ON DELETE CASCADE
);

-- ===============================
-- TABLE: pret
-- ===============================
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

-- ===============================
-- TABLE: reservation
-- ===============================
CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    id_livre INT NOT NULL,
    date_reservation DATE DEFAULT CURRENT_DATE,
    expire_le DATE,
    statut VARCHAR(50) DEFAULT 'en_attente' CHECK (statut IN ('en_attente', 'confirmee', 'expiree', 'annulee')),
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_reservation_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE,
    CONSTRAINT fk_reservation_livre FOREIGN KEY (id_livre) REFERENCES livre(id) ON DELETE CASCADE
);

-- ===============================
-- TABLE: type_action
-- ===============================
CREATE TABLE type_action (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP
);

-- ===============================
-- TABLE: historique_adherent
-- ===============================
CREATE TABLE historique_adherent (
    id SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    id_type_action INT NOT NULL,
    commentaire TEXT,
    date_action TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_historique_adherent FOREIGN KEY (id_adherent) REFERENCES adherent(id) ON DELETE CASCADE,
    CONSTRAINT fk_historique_type_action FOREIGN KEY (id_type_action) REFERENCES type_action(id) ON DELETE RESTRICT
);

-- ===============================
-- TABLE: configuration_quota
-- ===============================
CREATE TABLE configuration_quota (
    id SERIAL PRIMARY KEY,
    id_categorie_adherent INT NOT NULL,
    quota_max INT,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_configuration_categorie FOREIGN KEY (id_categorie_adherent) REFERENCES categorie_adherent(id) ON DELETE CASCADE
);

-- ===============================
-- TABLE: employe (hérite de personne)
-- ===============================
CREATE TABLE employe (
    id SERIAL PRIMARY KEY,
    poste VARCHAR(100),
    date_embauche DATE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_employe_personne FOREIGN KEY (id) REFERENCES personne(id) ON DELETE CASCADE
);

-- ===============================
-- TABLE: utilisateur (lié à employe)
-- ===============================
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    id_employe INT UNIQUE NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,
    CONSTRAINT fk_utilisateur_employe FOREIGN KEY (id_employe) REFERENCES employe(id) ON DELETE CASCADE
);

-- ===============================
-- TABLE: role
-- ===============================
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-- ===============================
-- TABLE: utilisateur_role (relation N:N)
-- ===============================
CREATE TABLE utilisateur_role (
    id_utilisateur INT NOT NULL,
    id_role INT NOT NULL,
    PRIMARY KEY (id_utilisateur, id_role),
    CONSTRAINT fk_utilisateur FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (id_role) REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    message TEXT NOT NULL,
    est_lu BOOLEAN DEFAULT FALSE,
    date_notification TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,

    CONSTRAINT fk_notification_adherent FOREIGN KEY (id_adherent)
        REFERENCES adherent(id) ON DELETE CASCADE
);

CREATE TABLE sanction (
    id SERIAL PRIMARY KEY,
    id_adherent INT NOT NULL,
    type_sanction VARCHAR(100) NOT NULL, 
    description TEXT,
    date_debut DATE NOT NULL,
    date_fin DATE,
    est_active BOOLEAN DEFAULT TRUE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_suppression TIMESTAMP,

    CONSTRAINT fk_sanction_adherent FOREIGN KEY (id_adherent)
        REFERENCES adherent(id) ON DELETE CASCADE
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


ALTER TABLE adherent ADD COLUMN mot_de_passe VARCHAR(255);
    ALTER TABLE role
    ADD COLUMN date_ajout TIMESTAMP DEFAULT now();

ALTER TABLE role ADD COLUMN date_suppression TIMESTAMP;

