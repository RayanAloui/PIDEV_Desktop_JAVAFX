-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : lun. 03 mars 2025 à 16:37
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
(1, 'sprtif', 'sport', '1:30 h', 'zidane coach', '01/01/2026', 'Marsa', 'Promenade et jeux en plein air', 'en cours\r\n');

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
(13, 1, 12222.00, '2025-03-05', 'AREGNT', 'ADZD', 'en cours'),
(14, 1, 11111.00, '2025-03-14', 'bubiu', 'ezaec', 'fafaz'),
(15, 1, 1111111.00, '2025-03-19', 'AZFA', 'AFFEAF', 'AFZ'),
(16, 1, 1111111.00, '2025-03-12', 'AZA', 'ADZD', 'EEE'),
(17, 1, 10.00, '2025-03-20', 'AAEZ', 'AZEZAE', 'ADZ');

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `heure` varchar(10) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `activite` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `notification`
--

INSERT INTO `notification` (`id`, `date`, `heure`, `username`, `activite`) VALUES
(1, '2025-02-22', '22:01', 'riadh', 'signed in '),
(2, '2025-02-22', '22:01', 'riadh', 'Logged in '),
(3, '2025-02-22', '22:01', 'RIADH', 'Edit Profile'),
(4, '2025-02-22', '22:02', 'RIADH', 'Changed password '),
(5, '2025-02-22', '22:02', 'RIADH', 'Logged in '),
(6, '2025-02-22', '22:23', 'louayXXX', 'Logged in '),
(7, '2025-02-22', '22:30', 'louayXXX', 'Logged in '),
(8, '2025-02-22', '22:42', 'louayXXX', 'Logged in '),
(9, '2025-02-22', '22:43', 'louayXXX', 'Logged in '),
(10, '2025-02-22', '22:44', 'louayXXX', 'Logged in '),
(11, '2025-02-22', '22:46', 'louayXXX', 'Logged in '),
(12, '2025-02-22', '22:50', 'louayXXX', 'Logged in '),
(13, '2025-02-22', '22:50', 'louayXXX', 'Logged in '),
(14, '2025-02-22', '22:51', 'louayXXX', 'Logged in '),
(15, '2025-02-22', '23:01', 'louayXXX', 'Logged in '),
(16, '2025-02-22', '23:10', 'louayXXX', 'Logged in '),
(17, '2025-02-22', '23:33', 'louayXXX', 'Logged in '),
(18, '2025-02-22', '23:34', 'louayXXX', 'Logged in '),
(19, '2025-02-22', '23:35', 'louayXXX', 'Logged in '),
(20, '2025-02-22', '23:36', 'louayXXX', 'Logged in '),
(21, '2025-02-22', '23:52', 'louayXXX', 'Logged in '),
(22, '2025-02-22', '23:55', 'louayXXX', 'Logged in '),
(23, '2025-02-23', '00:14', 'louayXXX', 'Logged in '),
(24, '2025-02-23', '00:15', 'louayXXX', 'Logged in '),
(25, '2025-02-23', '00:21', 'louayXXX', 'Logged in '),
(26, '2025-02-23', '00:24', 'louayXXX', 'Logged in '),
(27, '2025-02-23', '00:25', 'louayXXX', 'Logged in '),
(28, '2025-02-23', '00:28', 'louayXXX', 'Logged in '),
(29, '2025-02-23', '00:32', 'louayXXX', 'Logged in '),
(30, '2025-02-23', '00:33', 'louayXXX', 'Logged in '),
(31, '2025-02-23', '00:34', 'louayXXX', 'Logged in '),
(32, '2025-02-23', '00:35', 'louay', 'Edit Profile'),
(33, '2025-02-23', '01:09', 'louay', 'Logged in '),
(34, '2025-02-23', '01:15', 'louay', 'Logged in '),
(35, '2025-02-23', '01:50', 'louayXX', 'Edit Profile'),
(36, '2025-02-24', '13:54', 'rriadh', 'signed in '),
(37, '2025-02-24', '13:55', 'rriadh', 'Logged in '),
(38, '2025-02-24', '13:55', 'rriadh', 'Logged in '),
(39, '2025-02-24', '13:55', 'rriadh', 'Logged in '),
(40, '2025-02-24', '13:55', 'RIADH', 'Edit Profile'),
(41, '2025-02-24', '13:56', 'RIADH', 'Changed password '),
(42, '2025-02-24', '13:56', 'RIADH', 'Logged in '),
(43, '2025-02-24', '14:01', 'tt', 'Created account '),
(44, '2025-02-24', '17:30', 'louay', 'Created account '),
(45, '2025-02-24', '17:31', 'louay', 'Logged in '),
(46, '2025-02-24', '17:31', 'louay', 'Logged in '),
(47, '2025-02-24', '17:31', 'LOUAY', 'Edit Profile'),
(48, '2025-02-24', '17:32', 'LOUAY', 'Changed password '),
(49, '2025-02-24', '17:32', 'LOUAY', 'Logged in '),
(50, '2025-02-25', '09:29', 'louaytest', 'Created account '),
(51, '2025-02-25', '09:30', 'louaytest', 'Logged in '),
(52, '2025-02-25', '09:30', 'louaytest', 'Logged in '),
(53, '2025-02-25', '09:31', 'LOUAY', 'Edit Profile'),
(54, '2025-02-25', '09:32', 'LOUAY', 'Changed password '),
(55, '2025-02-25', '09:32', 'LOUAY', 'Logged in '),
(56, '2025-02-26', '14:53', 'x', 'Created account '),
(57, '2025-02-26', '15:12', 'aa', 'Created account '),
(58, '2025-02-26', '15:14', 'aa', 'Created account '),
(59, '2025-02-26', '15:16', 'aa', 'Logged in '),
(60, '2025-02-26', '15:20', 'aa', 'Logged in '),
(61, '2025-02-26', '15:29', 'louayXX', 'Logged in '),
(62, '2025-02-26', '15:30', 'louayXX', 'Logged in '),
(63, '2025-02-26', '15:30', 'louayXX', 'Changed password'),
(64, '2025-02-26', '15:31', 'louayXX', 'Logged in '),
(65, '2025-02-26', '15:31', 'louayXX', 'Changed password'),
(66, '2025-02-26', '15:31', 'louayXX', 'Logged in '),
(67, '2025-02-26', '15:38', 'louayXX', 'Logged in '),
(68, '2025-02-26', '15:41', 'louayXX', 'Logged in '),
(69, '2025-02-26', '15:41', 'louayXX', 'Changed password'),
(70, '2025-02-26', '15:41', 'louayXX', 'Logged in '),
(71, '2025-02-26', '15:46', 'louayXX', 'Logged in '),
(72, '2025-02-26', '15:46', 'louayXX', 'Changed password'),
(73, '2025-02-26', '20:56', 'EL AMARI LOUAY', 'Connected with API google '),
(74, '2025-02-26', '21:07', 'louayXX', 'Logged in '),
(75, '2025-02-26', '21:12', 'CMD LOUAY !!', 'Connected with API google '),
(76, '2025-02-26', '21:14', NULL, 'Connected with API google '),
(77, '2025-02-26', '21:14', NULL, 'Connected with API google '),
(78, '2025-02-26', '21:15', 'EL AMARI LOUAY', 'Connected with API google '),
(79, '2025-02-26', '21:19', 'CMD LOUAY !!', 'Connected with API google '),
(80, '2025-02-26', '21:27', NULL, 'Connected with API google '),
(81, '2025-02-26', '21:27', 'orphen care', 'Connected with API google '),
(82, '2025-02-26', '21:28', NULL, 'Connected with API google '),
(83, '2025-02-26', '21:28', 'EL AMARI LOUAY', 'Connected with API google '),
(84, '2025-02-26', '21:52', 'CMD LOUAY !!', 'Connected with API google '),
(85, '2025-02-26', '21:53', 'CMD LOUAY !!', 'Connected with API google '),
(86, '2025-02-26', '21:54', 'CMD LOUAY !!', 'Connected with API google '),
(87, '2025-03-02', '13:19', 'CMD LOUAY !!', 'Connected with API google '),
(88, '2025-03-02', '13:34', 'AMIR', 'Created account '),
(89, '2025-03-02', '13:38', 'louayXX', 'Logged in '),
(90, '2025-03-02', '13:43', 'louayXX', 'Logged in '),
(91, '2025-03-02', '13:43', 'louayXX', 'Logged in '),
(92, '2025-03-02', '13:45', 'louayXX', 'Logged in '),
(93, '2025-03-02', '13:47', 'louayXX', 'Logged in '),
(94, '2025-03-02', '13:48', 'louayXX', 'Logged in '),
(95, '2025-03-02', '13:49', 'louayXX', 'Logged in '),
(96, '2025-03-02', '13:59', 'louayXX', 'Logged in '),
(97, '2025-03-02', '14:03', 'louayXX', 'Logged in '),
(98, '2025-03-02', '14:03', 'louay', 'Edit Profile'),
(99, '2025-03-02', '14:04', 'louay', 'Logged in '),
(100, '2025-03-02', '14:07', 'louay', 'Logged in '),
(101, '2025-03-02', '14:10', 'louay', 'Logged in '),
(102, '2025-03-02', '14:13', 'louay', 'Logged in '),
(103, '2025-03-02', '14:15', 'louay', 'Logged in '),
(104, '2025-03-02', '14:24', 'louay', 'Logged in '),
(105, '2025-03-02', '14:25', 'louay', 'Logged in '),
(106, '2025-03-02', '14:25', 'LOUAY', 'Edit Profile'),
(107, '2025-03-02', '14:28', 'LOUAY', 'Logged in '),
(108, '2025-03-02', '14:28', 'LOUAY', 'Changed password'),
(109, '2025-03-02', '14:28', 'LOUAY', 'Logged in '),
(110, '2025-03-02', '14:30', 'LOUAY', 'Logged in '),
(111, '2025-03-02', '14:45', 'CMD LOUAY !!', 'Connected with API google '),
(112, '2025-03-02', '15:01', 'LOUAY', 'Logged in '),
(113, '2025-03-02', '15:05', 'LOUAY', 'Logged in '),
(114, '2025-03-02', '15:06', 'LOUAY', 'Logged in '),
(115, '2025-03-02', '15:06', 'LOUAY', 'Logged in '),
(116, '2025-03-02', '15:07', 'LOUAY', 'Logged in '),
(117, '2025-03-02', '15:08', 'LOUAY', 'Logged in '),
(118, '2025-03-02', '15:09', 'LOUAY', 'Logged in '),
(119, '2025-03-02', '15:12', 'LOUAY', 'Logged in '),
(120, '2025-03-02', '15:13', 'LOUAY', 'Logged in '),
(121, '2025-03-02', '15:14', 'LOUAY', 'Logged in '),
(122, '2025-03-02', '15:15', 'LOUAY', 'Edit Profile'),
(123, '2025-03-02', '15:16', 'LOUAY', 'Logged in '),
(124, '2025-03-02', '15:17', 'LOUAY', 'Logged in '),
(125, '2025-03-02', '15:17', 'LOUAY', 'Logged in '),
(126, '2025-03-02', '15:18', 'LOUAY', 'Logged in '),
(127, '2025-03-02', '15:20', 'LOUAY', 'Logged in '),
(128, '2025-03-02', '15:22', 'LOUAY', 'Logged in '),
(129, '2025-03-02', '15:23', 'LOUAY', 'Logged in '),
(130, '2025-03-02', '15:23', 'CMD LOUAY !!', 'Connected with API google '),
(131, '2025-03-02', '15:24', 't', 'Created account '),
(132, '2025-03-02', '15:24', 't', 'Logged in '),
(133, '2025-03-02', '15:25', 't', 'Logged in '),
(134, '2025-03-02', '15:25', 'aa', 'Created account '),
(135, '2025-03-02', '15:26', 'aa', 'Logged in '),
(136, '2025-03-02', '15:26', 'aa', 'Edit Profile'),
(137, '2025-03-02', '15:28', 'LOUAY', 'Logged in '),
(138, '2025-03-02', '15:31', 'LOUAY', 'Logged in '),
(139, '2025-03-02', '15:34', 'LOUAY', 'Logged in '),
(140, '2025-03-02', '15:34', 'LOUAY', 'Logged in '),
(141, '2025-03-02', '15:36', 'LOUAY', 'Logged in '),
(142, '2025-03-02', '15:40', 'LOUAY', 'Logged in '),
(143, '2025-03-02', '15:46', 'EL AMARI LOUAY', 'Connected with API google '),
(144, '2025-03-02', '15:47', 'rayen', 'Created account '),
(145, '2025-03-02', '15:47', 'rayen', 'Logged in '),
(146, '2025-03-03', '13:11', 'test', 'Created account '),
(147, '2025-03-03', '13:11', 'test', 'Logged in '),
(148, '2025-03-03', '13:11', 'test', 'Edit Profile'),
(149, '2025-03-03', '13:12', 'test', 'Changed password'),
(150, '2025-03-03', '13:12', 'test', 'Logged in '),
(151, '2025-03-03', '13:12', 'test', 'Edit Profile'),
(152, '2025-03-03', '13:13', 'test', 'Edit Profile'),
(153, '2025-03-03', '13:14', 'test', 'Edit Profile'),
(154, '2025-03-03', '13:15', 'orphen care', 'Connected with API google '),
(155, '2025-03-03', '13:20', 'TEST', 'Edit Profile'),
(156, '2025-03-03', '13:26', 'CMD LOUAY !!', 'Connected with API google '),
(157, '2025-03-03', '13:28', 'CMD LOUAY !!', 'Connected with API google '),
(158, '2025-03-03', '13:29', 'CMD LOUAY !!', 'Connected with API google '),
(159, '2025-03-03', '13:30', 'CMD LOUAY !!', 'Connected with API google '),
(160, '2025-03-03', '14:17', 'LOUAY', 'Logged in '),
(161, '2025-03-03', '14:21', 'LOUAY', 'Logged in '),
(162, '2025-03-03', '14:23', 'LOUAY', 'Logged in '),
(163, '2025-03-03', '14:25', 'LOUAY', 'Logged in '),
(164, '2025-03-03', '14:27', 'LOUAY', 'Logged in '),
(165, '2025-03-03', '14:36', 'LOUAY', 'Logged in '),
(166, '2025-03-03', '14:38', 'LOUAY', 'Logged in '),
(167, '2025-03-03', '14:39', 'LOUAY', 'Logged in '),
(168, '2025-03-03', '14:42', 'LOUAY', 'Logged in '),
(169, '2025-03-03', '16:13', 'LOUAY', 'Logged in '),
(170, '2025-03-03', '16:15', 'LOUAY', 'Logged in '),
(171, '2025-03-03', '16:18', 'LOUAY', 'Logged in '),
(172, '2025-03-03', '16:19', 'LOUAY', 'Logged in '),
(173, '2025-03-03', '16:23', 'LOUAY', 'Logged in '),
(174, '2025-03-03', '16:24', 'LOUAY', 'Logged in '),
(175, '2025-03-03', '16:26', 'CMD LOUAY !!', 'Connected with API google '),
(176, '2025-03-03', '16:32', 'LOUAY', 'Logged in '),
(177, '2025-03-03', '16:32', 'LOUAY', 'Logged in ');

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
(6, 'titiiiii', 'henry', '2000-02-05', 'M', 'LWF', 14),
(7, 'ZINIDINE', 'ZIZOU', '2025-02-11', 'M', 'JOUEUR', 11);

-- --------------------------------------------------------

--
-- Structure de la table `participant`
--

CREATE TABLE `participant` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL CHECK (`age` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `participant`
--

INSERT INTO `participant` (`id`, `nom`, `prenom`, `age`) VALUES
(1, 'Dupont', 'Jean', 25),
(2, 'Martin', 'Sophie', 30),
(3, 'Lambert', 'Paul', 22),
(6, 'riadh', 'gasmi', 55);

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
-- Structure de la table `reclamations`
--

CREATE TABLE `reclamations` (
  `id` int(11) NOT NULL,
  `mail` varchar(500) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `date` date NOT NULL,
  `statut` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reclamations`
--

INSERT INTO `reclamations` (`id`, `mail`, `description`, `date`, `statut`) VALUES
(1, 'user@example.com', 'Issue with product delivery', '2025-02-17', 'Pending'),
(3, 'louagjy@example.com', 'Issue with product deliveryyyyyyyxxxx', '2000-02-10', 'Pending'),
(5, 'aefaefaef@aeffa.aefeafae', 'afdfaef', '2025-02-17', 'traitee'),
(6, 'SARAh@gmail.com', 'JAIME PAS ', '2025-02-18', 'non traitee');

-- --------------------------------------------------------

--
-- Structure de la table `reponses`
--

CREATE TABLE `reponses` (
  `id` int(11) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `date` int(11) NOT NULL,
  `statut` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reponses`
--

INSERT INTO `reponses` (`id`, `description`, `date`, `statut`) VALUES
(1, 'reponse1', 1, 'en cours');

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
  `token` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `name`, `surname`, `telephone`, `email`, `password`, `role`, `isBlocked`, `isConfirmed`, `numberVerification`, `token`, `image`) VALUES
(1, 'LOUAY', 'AMARI', '24604748', 'louayamari63@gmail.com', 'VY_Kc;<=10', 'client', 0, 0, 516595, 3438, 'moi.jpg'),
(2, 'admin', 'admin', '00000000', 'admin@gmail.com', ':10', 'admin', 0, 0, 0, 0, NULL),
(40, 'rayen', 'aloui', '10200300', 'rayen@gmail.com', 'KdO\\^c10', 'client', 0, 0, 499567, 0, 'rayen.jpg'),
(41, 'TEST', 'test', '94872146', 'elamari.louay@esprit.tn', 'mlx610', 'client', 0, 1, 836342, 8358, 'youssef.jpg');

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
(11, 28, '2025-02-28', '12:12:00', 'Mignon', 'En attente'),
(12, 29, '2025-03-21', '12:13:00', 'mignon', 'En attente'),
(13, 25, '2025-03-11', '12:00:00', 'check', 'En attente');

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
  `adresse` text NOT NULL,
  `cin` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `visiteurs`
--

INSERT INTO `visiteurs` (`id`, `nom`, `prenom`, `email`, `tel`, `adresse`, `cin`) VALUES
(25, 'monji', 'slim', 'momo.slimane@gmail.com', 12345678, 'nehj zimbabwe', '09275830'),
(26, 'Jihene', 'Zeineb', 'zinouba@raka.com', 12345678, 'nehj zimbabwe', '05814306'),
(28, 'Sami Ben', 'Abdelkader', 'sasa@gmail.com', 12345678, 'RUE RUE', '12345988'),
(29, 'Malki', 'Youssef', 'y.m@gmail.com', 12345678, '06 RUE des mignons', '15026454'),
(31, 'LOUAY', 'AMARI', 'louayamari63@gmail.com', 24604748, 'TUNIS', '12312333');

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
-- Index pour la table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `orphelins`
--
ALTER TABLE `orphelins`
  ADD PRIMARY KEY (`idO`),
  ADD KEY `idTuteur` (`idTuteur`);

--
-- Index pour la table `participant`
--
ALTER TABLE `participant`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `personne`
--
ALTER TABLE `personne`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reclamations`
--
ALTER TABLE `reclamations`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reponses`
--
ALTER TABLE `reponses`
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
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cin` (`cin`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `activite`
--
ALTER TABLE `activite`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `donateur`
--
ALTER TABLE `donateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT pour la table `dons`
--
ALTER TABLE `dons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=178;

--
-- AUTO_INCREMENT pour la table `orphelins`
--
ALTER TABLE `orphelins`
  MODIFY `idO` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `participant`
--
ALTER TABLE `participant`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `personne`
--
ALTER TABLE `personne`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `reclamations`
--
ALTER TABLE `reclamations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `reponses`
--
ALTER TABLE `reponses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `tuteurs`
--
ALTER TABLE `tuteurs`
  MODIFY `idT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT pour la table `visites`
--
ALTER TABLE `visites`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `visiteurs`
--
ALTER TABLE `visiteurs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

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

--
-- Contraintes pour la table `visites`
--
ALTER TABLE `visites`
  ADD CONSTRAINT `FK_id_visiteur` FOREIGN KEY (`id_visiteur`) REFERENCES `visiteurs` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
