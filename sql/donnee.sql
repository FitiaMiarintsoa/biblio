-- Insertion catégories d'adhérents
INSERT INTO categorie_adherent (nom, description) VALUES
('Standard', 'Adhérent standard avec quota de 5 livres'),
('Premium', 'Adhérent premium avec quota de 10 livres');

-- Insertion personnes (bibliothécaires et adhérents)
INSERT INTO personne (nom, prenom, email, adresse, date_naissance) VALUES
('Dupont', 'Alice', 'alice.dupont@example.com', '123 rue A', '1980-01-15'), -- id=1
('Martin', 'Bob', 'bob.martin@example.com', '456 avenue B', '1990-06-20'), -- id=2
('Durand', 'Claire', 'claire.durand@example.com', '789 boulevard C', '1985-03-05'); -- id=3

-- Insertion adhérents (avec mot_de_passe ajouté)
INSERT INTO adherent (id, identifiant, id_categorie_adherent, quota_max, est_bloque, mot_de_passe) VALUES
(2, 'bobm', 1, 5, FALSE, 'motdepasse_bob'); -- Bob est adhérent

-- Insertion bibliothécaire
INSERT INTO bibliothecaire (id) VALUES
(1); -- Alice est bibliothécaire

-- Insertion livres
INSERT INTO livre (titre, auteur, genre, isbn, restriction_age) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupéry', 'Conte', '9780156012195', NULL),
('1984', 'George Orwell', 'Dystopie', '9780451524935', 16);

-- Insertion exemplaires (livres)
INSERT INTO exemplaire (id_livre, statut) VALUES
(1, 'disponible'),
(1, 'disponible'),
(2, 'disponible');

-- Insertion prêts (Bob emprunte un exemplaire)
INSERT INTO pret (id_adherent, id_exemplaire, date_emprunt, date_retour_prevue, est_prolonge) VALUES
(2, 1, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE + INTERVAL '7 days', FALSE);

-- Insertion réservations
INSERT INTO reservation (id_adherent, id_livre, expire_le, statut) VALUES
(2, 2, CURRENT_DATE + INTERVAL '5 days', 'en_attente');

-- Insertion types d'actions
INSERT INTO type_action (nom, description) VALUES
('Emprunt', 'Action d''emprunter un livre'),
('Retour', 'Action de retourner un livre');

-- Insertion roles (pour utilisateurs)
INSERT INTO role (nom, description) VALUES
('ROLE_ADMIN', 'Administrateur'),
('ROLE_BIBLIOTHECAIRE', 'Bibliothécaire'),
('ROLE_ADHERENT', 'Adhérent');

-- Insertion employé (lié à personne)
INSERT INTO employe (id, poste, date_embauche) VALUES
(1, 'Gestionnaire', '2010-05-10'); -- Alice en employé

-- Insertion utilisateur (lié à employé)
INSERT INTO utilisateur (username, mot_de_passe, id_employe, actif) VALUES
('alice', 'passwordAlice', 1, TRUE);

-- Liaison utilisateur-role
INSERT INTO utilisateur_role (id_utilisateur, id_role) VALUES
((SELECT id FROM utilisateur WHERE username='alice'), (SELECT id FROM role WHERE nom='ROLE_BIBLIOTHECAIRE'));

-- Ajout mot de passe adhérent (si tu veux connecter adhérent aussi)
UPDATE adherent SET mot_de_passe = 'motdepasse_bob' WHERE identifiant = 'bobm';
