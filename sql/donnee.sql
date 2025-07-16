INSERT INTO Profil (nom, description) VALUES
('Etudiant', 'Profil pour Etudiants avec acces limitE à 3 livres et rEservations rEduites'),
('Enseignant', 'Profil pour enseignants avec acces Etendu à 8 livres et prolongations autorisEes'),
('Professionnel', 'Profil pour professionnels avec acces standard à 5 livres et durEe d emprunt plus longue');

INSERT INTO personne (nom, prenom, email, adresse, date_naissance) VALUES
('Bensaid', 'Amine', 'amine.bensaid@bibliomail.com', '12 rue des Orangers, Casablanca', '1985-04-10'),
('El Khattabi', 'Sarah', 'sarah.elkhattabi@bibliomail.com', '34 avenue Hassan II, Rabat', '1992-11-22'),
('Moujahid', 'Youssef', 'youssef.moujahid@bibliomail.com', '56 rue Al Massira, Marrakech', '1987-06-17'),
('Benali', 'Nadia', 'nadia.benali@bibliomail.com', '78 boulevard Zerktouni, Fes', '1990-02-03'),
('Haddadi', 'Karim', 'karim.haddadi@bibliomail.com', '90 rue Mohammed V, Tanger', '1983-12-08'),
('Touhami', 'Salima', 'salima.touhami@bibliomail.com', '23 citE Ennakhil, Agadir', '1995-08-14'),
('El Mansouri', 'Rachid', 'rachid.elmansouri@bibliomail.com', '45 avenue Oujda, Oujda', '1980-05-29'),
('Zerouali', 'Amina', 'amina.zerouali@bibliomail.com', '67 rEsidence El Yassmine, Meknes', '1998-10-19');

INSERT INTO personne (nom, prenom, email, adresse, date_naissance) VALUES
('Jean', 'bibliothecaire', 'biblio@gmail.com', 'Antanimena', '1985-04-10');

INSERT INTO adherent (id_profil, id_personne, est_bloque) VALUES
(1, 1, FALSE),
(1, 2, FALSE),
(1, 3, FALSE),
(2, 4, FALSE),
(2, 5, FALSE),
(2, 6, FALSE),
(3, 7, FALSE),
(3, 8, FALSE);

INSERT INTO bibliothecaire (id_personne) VALUES
(9);

INSERT INTO role (nom, description) VALUES
('ROLE_BIBLIOTHECAIRE', 'Acces bibliothEcaire'),
('ROLE_ADHERENT', 'Acces adhErent');

INSERT INTO utilisateur (id_personne, username, mot_de_passe, actif) VALUES
(1, 'ETU001', 'ETU001', TRUE),
(2, 'ETU002', 'ETU002', TRUE),
(3, 'ETU003', 'ETU003', TRUE),
(4, 'ENS001', 'ENS001', TRUE),
(5, 'ENS002', 'ENS002', TRUE),
(6, 'ENS003', 'ENS003', TRUE),
(7, 'PROF001', 'PROF001', TRUE),
(8, 'PROF002', 'PROF002', TRUE),
(9, 'bibliothecaire', 'bibliothecaire', TRUE);

INSERT INTO utilisateur_role (id_utilisateur, id_role) VALUES
((SELECT id FROM utilisateur WHERE username = 'ETU001'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'ETU002'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'ETU003'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'ENS001'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'ENS002'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'ENS003'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'PROF001'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT')),
((SELECT id FROM utilisateur WHERE username = 'PROF002'), (SELECT id FROM role WHERE nom = 'ROLE_ADHERENT'));

INSERT INTO utilisateur_role (id_utilisateur, id_role) VALUES
((SELECT id FROM utilisateur WHERE username = 'bibliothecaire'), (SELECT id FROM role WHERE nom = 'ROLE_BIBLIOTHECAIRE'));

INSERT INTO livre (titre, auteur, genre, isbn, restriction_age) VALUES
('Les MisErables', 'Victor Hugo', 'LittErature classique', '9782070409189', NULL),
('L Etranger', 'Albert Camus', 'Philosophie', '9782070360022', NULL),
('Harry Potter à l Ecole des sorciers', 'J.K. Rowling', 'Jeunesse Fantastique', '9782070643026', NULL);


-- Exemplaires pour "Les MisErables" (ID livre = 1)
INSERT INTO exemplaire (id_livre, statut) VALUES
(1, 'disponible'),
(1, 'disponible'),
(1, 'disponible');

-- Exemplaires pour "L'Etranger" (ID livre = 2)
INSERT INTO exemplaire (id_livre, statut) VALUES
(2, 'disponible'),
(2, 'disponible');

-- Exemplaire pour "Harry Potter à l'Ecole des sorciers" (ID livre = 3)
INSERT INTO exemplaire (id_livre, statut) VALUES
(3, 'disponible');



INSERT INTO type_action (nom, description) VALUES
('emprunt', 'Action d''emprunter un livre'),
('retour', 'Action de retourner un livre'),
('reservation', 'Action de rEserver un livre');

INSERT INTO type_sanction (nom, description, penalite_jour) VALUES
('Retard de retour', 'Le livre n a pas EtE rendu à temps', 10),
('Perte de livre', 'Le livre empruntE a EtE perdu', 9),
('Comportement inappropriE', 'L adhErent a eu un comportement dEplacE', 8);


INSERT INTO abonnement (id_adherent, date_debut, date_fin) VALUES
(1, '2025-02-01', '2025-07-24'), -- ETU001
(2, '2025-02-01', '2025-07-01'), -- ETU002
(3, '2025-04-01', '2025-12-01'), -- ETU003
(4, '2025-07-01', '2026-07-01'), -- ENS001
(5, '2025-08-01', '2026-05-01'), -- ENS002
(6, '2025-07-01', '2026-07-01'), -- ENS003
(7, '2025-06-01', '2025-12-01'), -- PROF001
(8, '2024-10-01', '2025-06-01'); -- PROF002

-- Jours fEriEs
INSERT INTO jour_ferie (date_ferie, libelle) VALUES
('2025-07-19', 'Jour fEriE'),
('2025-07-26', 'Jour fEriE');

-- Dimanches
INSERT INTO jour_ferie (date_ferie, libelle) VALUES
('2025-07-13', 'Dimanche'),
('2025-07-20', 'Dimanche'),
('2025-07-27', 'Dimanche'),
('2025-08-03', 'Dimanche'),
('2025-08-10', 'Dimanche'),
('2025-08-17', 'Dimanche');

INSERT INTO configuration_quota 
(id_profil, quota_pret, quota_pret_place, quota_reservation, quota_prolongation, nb_jour) 
VALUES
(1, 2, 3, 1, 3, 7),
(2, 3, 4, 2, 5, 9),
(3, 4, 5, 3, 7, 12);

INSERT INTO configuration_jour_ouvre (direction_decalage) VALUES ('apres');
