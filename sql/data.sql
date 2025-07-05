-- Insertion des profils
INSERT INTO Profil (nom, description) VALUES
('Standard', 'Adhérent standard avec quota de 5 livres'),
('Premium', 'Adhérent premium avec quota de 10 livres');

-- Insertion des personnes (bibliothécaire + adhérents)
INSERT INTO personne (nom, prenom, email, adresse, date_naissance) VALUES
('Dupont', 'Alice', 'alice.dupont@example.com', '123 rue A', '1980-01-15'),  -- id = 1
('Martin', 'Bob', 'bob.martin@example.com', '456 avenue B', '1990-06-20'),   -- id = 2
('Durand', 'Claire', 'claire.durand@example.com', '789 boulevard C', '1985-03-05'); -- id = 3

-- Insertion d’un adhérent (Bob)
INSERT INTO adherent (id_profil, id_personne, est_bloque) VALUES
(1, 2, FALSE);

-- Insertion d’un bibliothécaire (Alice)
INSERT INTO bibliothecaire (id_personne) VALUES
(1);

-- Insertion des rôles
INSERT INTO role (nom, description) VALUES
('ROLE_BIBLIOTHECAIRE', 'Accès bibliothécaire'),
('ROLE_ADHERENT', 'Accès adhérent');

-- Insertion des utilisateurs (sans champ "role")
INSERT INTO utilisateur (id_personne, username, mot_de_passe, actif) VALUES
(1, 'alice', 'passwordAlice', TRUE),  -- bibliothécaire
(2, 'bobm', 'motdepasse_bob', TRUE);  -- adhérent

-- Liaison utilisateur <-> rôle
INSERT INTO utilisateur_role (id_utilisateur, id_role) VALUES
((SELECT id FROM utilisateur WHERE username = 'alice'), (SELECT id FROM role WHERE nom = 'ROLE_BIBLIOTHECAIRE')),
((SELECT id FROM utilisateur WHERE username = 'bobm'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT'));

-- Insertion de livres
INSERT INTO livre (titre, auteur, genre, isbn, restriction_age) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupéry', 'Conte', '9780156012195', NULL),
('1984', 'George Orwell', 'Dystopie', '9780451524935', 16);

-- Insertion d’exemplaires
INSERT INTO exemplaire (id_livre, statut) VALUES
(1, 'disponible'),
(1, 'disponible'),
(2, 'disponible');

-- Insertion d’un prêt (Bob emprunte un exemplaire)
INSERT INTO pret (id_adherent, id_exemplaire, date_emprunt, date_retour_prevue, est_prolonge) VALUES
((SELECT id FROM adherent WHERE id_personne = 2), 1, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE + INTERVAL '7 days', FALSE);

-- Insertion d’une réservation
INSERT INTO reservation (id_adherent, id_livre, expire_le, statut) VALUES
((SELECT id FROM adherent WHERE id_personne = 2), 2, CURRENT_DATE + INTERVAL '5 days', 'en_attente');

-- Insertion de types d’actions
INSERT INTO type_action (nom, description) VALUES
('emprunt', 'Action d''emprunter un livre'),
('retour', 'Action de retourner un livre'),
('reservation', 'Action de réserver un livre');

-- Insertion de configuration de quota
INSERT INTO configuration_quota (id_profil, quota_pret, quota_prolongation, nb_jour) VALUES
(1, 5, 1, 7),
(2, 10, 2, 10);
