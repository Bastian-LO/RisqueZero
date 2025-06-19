-- Désactiver temporairement les contraintes
SET FOREIGN_KEY_CHECKS = 0;

-- Vider les tables
TRUNCATE TABLE affectation;
TRUNCATE TABLE dps_competence;
TRUNCATE TABLE secouriste_competence;
TRUNCATE TABLE dispos;
TRUNCATE TABLE dps;
TRUNCATE TABLE secouriste;
TRUNCATE TABLE utilisateur;
TRUNCATE TABLE sport;
TRUNCATE TABLE site;
TRUNCATE TABLE competence_requis;
TRUNCATE TABLE competence;

-- Réactiver les contraintes
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Création des utilisateurs
INSERT INTO utilisateur (login, mot_de_passe, est_admin) VALUES
('admin','mdp_admin', 1),
('user1','user123', 0),
('user2', 'user456', 0);

-- 2. Création des secouristes (avec id_utilisateur)
INSERT INTO secouriste (id_utilisateur, nom, prenom, date_naissance, email, tel, adresse) VALUES
(1, 'Admin', 'System', '01/01/1980', 'admin@system.com', '0612345678', 'Siège social'),
(2, 'Dupont', 'Jean', '15/05/1990', 'jean@email.com', '0698765432', '12 Rue des Lilas'),
(3, 'Martin', 'Sophie', '22/08/1995', 'sophie@email.com', '0678912345', '34 Avenue des Champs');

-- 3. Compétences
INSERT INTO competence (intitule) VALUES 
('PSC1'),
('PSE1'),
('PSE2');

-- 4. Prérequis
INSERT INTO competence_requis (competence_principale, competence_requise) VALUES
('PSE1', 'PSC1'),
('PSE2', 'PSE1');

-- 5. Sites
INSERT INTO site (code, nom, longitude, latitude) VALUES
('SITE1', 'Stade Municipal', 2.3522, 48.8566),
('SITE2', 'Piscine Olympique', 2.3364, 48.8841);

-- 6. Sports
INSERT INTO sport (code, nom) VALUES
('FOOT', 'Football'),
('NAT', 'Natation');

-- 7. Compétences des secouristes
INSERT INTO secouriste_competence (id_sec, competence) VALUES
(1, 'PSC1'),
(1, 'PSE1'),
(1, 'PSE2'),
(2, 'PSC1'),
(3, 'PSE1');

-- 8. Disponibilités
INSERT INTO dispos (id_sec, date_dispo, heure_debut, heure_fin) VALUES
(2, '2023-06-15', '08:00', '12:00'),
(3, '2023-06-15', '09:00', '17:00');

-- 9. DPS (Dispositifs Prévisionnels de Secours)
INSERT INTO dps (id, horaire_depart, horaire_fin, date_event, id_site, id_sport) VALUES
(1, '08:00', '12:00', '2023-06-15', 'SITE1', 'FOOT'),
(2, '09:00', '17:00', '2023-06-15', 'SITE2', 'NAT');

-- 10. Compétences requises pour les DPS
INSERT INTO dps_competence (id_dps, competence) VALUES
(1, 'PSC1'),
(2, 'PSE1');

-- 11. Affectations
INSERT INTO affectation (id_sec, id_dps, competence) VALUES
(2, 1, 'PSC1'),
(3, 2, 'PSE1');