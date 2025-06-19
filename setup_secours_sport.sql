CREATE DATABASE IF NOT EXISTS db_SAE;
USE db_SAE;

-- D'abord créer les tables sans dépendances
CREATE TABLE IF NOT EXISTS competence (
    intitule VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS site (
    code VARCHAR(20) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    longitude FLOAT NOT NULL CHECK (longitude BETWEEN -180 AND 180),
    latitude FLOAT NOT NULL CHECK (latitude BETWEEN -90 AND 90)
);

CREATE TABLE IF NOT EXISTS sport (
    code VARCHAR(20) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

-- Table utilisateur (créez-la d'abord)
CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    est_admin BOOLEAN NOT NULL DEFAULT 0
);

-- Table secouriste (modifiée pour référencer l'utilisateur)
CREATE TABLE IF NOT EXISTS secouriste (
    id_utilisateur INT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    date_naissance VARCHAR(10) NOT NULL CHECK (date_naissance REGEXP '^\\d{2}/\\d{2}/\\d{4}$'),
    email VARCHAR(255) NOT NULL CHECK (email REGEXP '^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$'),
    tel VARCHAR(20) NOT NULL CHECK (tel REGEXP '^0\\d{9}$'),
    adresse TEXT NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- Puis créer dispos qui référence secouriste
CREATE TABLE IF NOT EXISTS dispos (
    id_sec INT,
    date_dispo DATE NOT NULL,
    heure_debut VARCHAR(5) NOT NULL CHECK (heure_debut REGEXP '^\\d{2}:\\d{2}$'),
    heure_fin VARCHAR(5) NOT NULL CHECK (heure_fin REGEXP '^\\d{2}:\\d{2}$'),
    PRIMARY KEY (id_sec, date_dispo, heure_debut, heure_fin),
    FOREIGN KEY (id_sec) REFERENCES secouriste(id_utilisateur) ON DELETE CASCADE
);

-- Continuer avec les autres tables
CREATE TABLE competence_requis (
    competence_principale VARCHAR(255) NOT NULL,
    competence_requise VARCHAR(255) NOT NULL,
    PRIMARY KEY (competence_principale, competence_requise),
    FOREIGN KEY (competence_principale) REFERENCES competence(intitule) ON DELETE CASCADE,
    FOREIGN KEY (competence_requise) REFERENCES competence(intitule) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS secouriste_competence (
    id_sec INT,
    competence VARCHAR(255),
    PRIMARY KEY (id_sec, competence),
    FOREIGN KEY (id_sec) REFERENCES secouriste(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (competence) REFERENCES competence(intitule) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dps (
    id BIGINT PRIMARY KEY,
    horaire_depart VARCHAR(5) NOT NULL CHECK (horaire_depart REGEXP '^\\d{2}:\\d{2}$'),
    horaire_fin VARCHAR(5) NOT NULL CHECK (horaire_fin REGEXP '^\\d{2}:\\d{2}$'),
    date_event DATE NOT NULL,
    id_site VARCHAR(20) NOT NULL,
    id_sport VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_site) REFERENCES site(code) ON DELETE CASCADE,
    FOREIGN KEY (id_sport) REFERENCES sport(code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dps_competence (
    id_dps BIGINT,
    competence VARCHAR(255),
    PRIMARY KEY (id_dps, competence),
    FOREIGN KEY (id_dps) REFERENCES dps(id) ON DELETE CASCADE,
    FOREIGN KEY (competence) REFERENCES competence(intitule) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS affectation (
    id_sec INT NOT NULL,
    id_dps BIGINT NOT NULL,
    competence VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_sec, id_dps, competence),
    FOREIGN KEY (id_sec) REFERENCES secouriste(id_utilisateur) ON DELETE CASCADE,
    FOREIGN KEY (id_dps) REFERENCES dps(id) ON DELETE CASCADE,
    FOREIGN KEY (competence) REFERENCES competence(intitule) ON DELETE CASCADE
);