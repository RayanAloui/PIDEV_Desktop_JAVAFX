-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 16 fév. 2025 à 21:05
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `esprit`
--

-- --------------------------------------------------------

--
-- Structure de la table `activite`
--

CREATE TABLE `activite` (
  `id` int(255) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `categorie` varchar(255) NOT NULL,
  `duree` varchar(255) NOT NULL,
  `nom_du_tuteur` varchar(255) NOT NULL,
  `date_activite` varchar(255) NOT NULL,
  `lieu` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `statut` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `activite`
--

INSERT INTO `activite` (`id`, `nom`, `categorie`, `duree`, `nom_du_tuteur`, `date_activite`, `lieu`, `description`, `statut`) VALUES
(1, 'sprtif', 'sport', '1:30 h', 'zidane', '01/01/2026', 'Marsa', 'Promenade et jeux en plein air', 'en cours\r\n'),
(6, 'ye', 'Éducation', 'ye', 'ye', 'ye', 'ye', 'ye', 'terminée');

-- --------------------------------------------------------

--
-- Structure de la table `donateur`
--

CREATE TABLE `donateur` (
  `id` int(11) NOT NULL,
  `nom` varchar(25) NOT NULL,
  `prenom` varchar(25) NOT NULL,
  `email` varchar(30) NOT NULL,
  `telephone` int(8) NOT NULL,
  `adresse` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `donateur`
--

INSERT INTO `donateur` (`id`, `nom`, `prenom`, `email`, `telephone`, `adresse`) VALUES
(1, 'ahmed', 'ben ali', 'ahmed@gmail.com', 28488428, '06 Rue esprit'),
(2, 'SLIM', 'chiboub', 'slim@gmail.com', 12312312, '06 Rue esprit'),
(3, 'belahssan', 'trabelssi', 'belha@gmail.com', 28488428, '06 Rue esprit'),
(5, 'leila', 'ben ali', 'leila@gmail.com', 28488428, '06 Rue esprit'),
(9, 'nessrine', 'ben ali', 'nessrine@gmail.com', 28488428, '06 Rue esprit'),
(12, 'sakherr', 'el matri', 'sakher@gmail.com', 28488428, '06 Rue esprit'),
(15, 'louayyyyy', 'amari', 'louay@gmail.com', 99888777, 'ttt'),
(17, 'xx', 'xx', 'x@gmail.com', 99888777, 'x');

-- --------------------------------------------------------

--
-- Structure de la table `dons`
--

CREATE TABLE `dons` (
  `id` int(11) NOT NULL,
  `donateur_id` int(11) DEFAULT NULL,
  `montant` decimal(10,2) NOT NULL,
  `date_don` date NOT NULL,
  `type_don` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `statut` varchar(50) DEFAULT 'en attente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `dons`
--

INSERT INTO `dons` (`id`, `donateur_id`, `montant`, `date_don`, `type_don`, `description`, `statut`) VALUES
(1, 1, 200.00, '2025-02-01', 'materiel', 'je fait ce don pour des orphelins', 'en attente'),
(8, 2, 300.00, '2025-02-06', 'liquide', 'IEIHFI', 'EN cours'),
(10, 15, 1000.00, '2025-02-19', 'argent', 'aa', 'en attente'),
(11, 15, 102.00, '2025-02-11', 'aaa', 'aa', 'aa');

-- --------------------------------------------------------

--
-- Structure de la table `orphelins`
--

CREATE TABLE `orphelins` (
  `idO` int(11) NOT NULL,
  `nomO` varchar(100) NOT NULL,
  `prenomO` varchar(100) NOT NULL,
  `dateNaissance` date NOT NULL,
  `sexe` enum('M','F') NOT NULL,
  `situationScolaire` varchar(255) DEFAULT NULL,
  `idTuteur` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `orphelins`
--

INSERT INTO `orphelins` (`idO`, `nomO`, `prenomO`, `dateNaissance`, `sexe`, `situationScolaire`, `idTuteur`) VALUES
(1, 'bensalah', 'mouhib', '2004-04-10', 'M', 'étudiant', 3),
(2, 'benhamadi', 'mohamed', '2007-11-05', 'M', 'lycéen', 3),
(6, 'titiiiii', 'henry', '2000-02-05', 'M', 'LWF', 14);

-- --------------------------------------------------------

--
-- Structure de la table `personne`
--

CREATE TABLE `personne` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `personne`
--

INSERT INTO `personne` (`id`, `nom`, `prenom`) VALUES
(1, 'louay_test1', 'louay_test1'),
(2, 'Mohamedd', 'ben arar');

-- --------------------------------------------------------

--
-- Structure de la table `tuteurs`
--

CREATE TABLE `tuteurs` (
  `idT` int(11) NOT NULL,
  `cinT` varchar(20) NOT NULL,
  `nomT` varchar(100) NOT NULL,
  `prenomT` varchar(100) NOT NULL,
  `telephoneT` varchar(20) DEFAULT NULL,
  `adresseT` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `tuteurs`
--

INSERT INTO `tuteurs` (`idT`, `cinT`, `nomT`, `prenomT`, `telephoneT`, `adresseT`) VALUES
(3, '15026623', 'aloui', 'rayen', '94875412', 'Rue de Rome'),
(7, '14895236', 'bensalah', 'imed', '57485236', 'Rue de faucon'),
(9, '15236988', 'amdouni', 'mouhib', '92487563', 'Rue de Goulette'),
(11, '14895280', 'benmoulehom', 'samehhh', '94875415', 'Rue de Qatar'),
(12, '15029984', 'malkiii', 'youssef', '28754956', 'Rue de Marsa'),
(14, '12596625', 'lewy', 'lewy', '99888777', 'gassrine');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `telephone` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `isBlocked` int(11) NOT NULL,
  `isConfirmed` int(11) NOT NULL,
  `numberVerification` int(11) NOT NULL,
  `token` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `name`, `surname`, `telephone`, `email`, `password`, `role`, `isBlocked`, `isConfirmed`, `numberVerification`, `token`) VALUES
(44, 'admin', 'topadmin', '', 'admin@gmail.com', '0', 'admin', 0, 0, 0, 0),
(45, 'louayyy', 'amari', '24604748', 'louayamari63@gmail.com', '12345', 'client', 0, 0, 875822, 0),
(46, 'rayen', 'aloui', '55444111', 'rayen@gmail.com', '12345', 'tuteur', 0, 0, 908739, 0),
(51, 'youssef', 'malki', '99250700', 'youssef@gmail.com', '12345', 'visiteur', 0, 0, 399820, 0),
(52, 'Alice', 'Dupont', '12345678', 'alice@gmail.com', 'password1', 'client', 0, 0, 123456, 0),
(53, 'Bob', 'Martin', '23456789', 'bob@gmail.com', 'password2', 'client', 0, 0, 234567, 0),
(55, 'David', 'Leroy', '45678901', 'david@gmail.com', 'password4', 'donateur', 0, 0, 456789, 0),
(56, 'Eve', 'Moreau', '56789012', 'eve@gmail.com', 'password5', 'visiteur', 0, 0, 567890, 0),
(58, 'Grace', 'Laurent', '78901234', 'grace@gmail.com', 'password7', 'tuteur', 0, 0, 789012, 0);

-- --------------------------------------------------------

--
-- Structure de la table `visites`
--

CREATE TABLE `visites` (
  `id` int(11) NOT NULL,
  `id_visiteur` int(11) NOT NULL,
  `date` date NOT NULL,
  `heure` time NOT NULL,
  `motif` varchar(50) NOT NULL,
  `statut` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `visites`
--

INSERT INTO `visites` (`id`, `id_visiteur`, `date`, `heure`, `motif`, `statut`) VALUES
(9, 22, '2025-02-21', '12:30:00', 'bebe malade', 'Accepté'),
(10, 22, '2025-02-22', '11:32:00', 'Belhassen Trabelsi', 'En attente'),
(12, 24, '2025-02-11', '10:10:00', 'hhh', 'Accepté');

-- --------------------------------------------------------

--
-- Structure de la table `visiteurs`
--

CREATE TABLE `visiteurs` (
  `id` int(11) NOT NULL,
  `nom` varchar(25) NOT NULL,
  `prenom` varchar(25) NOT NULL,
  `email` varchar(40) NOT NULL,
  `tel` int(8) NOT NULL,
  `adresse` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `visiteurs`
--

INSERT INTO `visiteurs` (`id`, `nom`, `prenom`, `email`, `tel`, `adresse`) VALUES
(22, 'Samitou', 'Ben', 'sami@ben.com', 12345678, 'rue du nimporte quoi'),
(23, 'louay', 'amari', 'loouay@gmail.com', 24604748, 'gasserine');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `activite`
--
ALTER TABLE `activite`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `donateur`
--
ALTER TABLE `donateur`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `dons`
--
ALTER TABLE `dons`
  ADD PRIMARY KEY (`id`),
  ADD KEY `donateur_id` (`donateur_id`);

--
-- Index pour la table `orphelins`
--
ALTER TABLE `orphelins`
  ADD PRIMARY KEY (`idO`),
  ADD KEY `idTuteur` (`idTuteur`);

--
-- Index pour la table `personne`
--
ALTER TABLE `personne`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `tuteurs`
--
ALTER TABLE `tuteurs`
  ADD PRIMARY KEY (`idT`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `visites`
--
ALTER TABLE `visites`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_id_visiteur` (`id_visiteur`);

--
-- Index pour la table `visiteurs`
--
ALTER TABLE `visiteurs`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `activite`
--
ALTER TABLE `activite`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `donateur`
--
ALTER TABLE `donateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `dons`
--
ALTER TABLE `dons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `orphelins`
--
ALTER TABLE `orphelins`
  MODIFY `idO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `personne`
--
ALTER TABLE `personne`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `tuteurs`
--
ALTER TABLE `tuteurs`
  MODIFY `idT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT pour la table `visites`
--
ALTER TABLE `visites`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `visiteurs`
--
ALTER TABLE `visiteurs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `dons`
--
ALTER TABLE `dons`
  ADD CONSTRAINT `dons_ibfk_1` FOREIGN KEY (`donateur_id`) REFERENCES `donateur` (`id`);

--
-- Contraintes pour la table `orphelins`
--
ALTER TABLE `orphelins`
  ADD CONSTRAINT `orphelins_ibfk_1` FOREIGN KEY (`idTuteur`) REFERENCES `tuteurs` (`idT`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
