# 🧒 OrphanCare

**OrphanCare** est une application desktop développée en Java pour faciliter la gestion quotidienne des centres d'accueil d'orphelins. Elle offre une interface complète et intuitive pour gérer les utilisateurs, les orphelins, les visites, les donations, les réclamations et les activités.

## 📌 Modules principaux

- 👤 **Gestion des utilisateurs** : création, authentification, rôles, et permissions.
- 🧒 **Gestion des orphelins** : ajout, mise à jour, historique, affectation à un tuteur.
- 🏥 **Gestion des visites** : planification, enregistrement et suivi des visites.
- 💰 **Gestion des donations** : suivi des dons financiers ou matériels.
- 🗣️ **Gestion des réclamations** : dépôt, traitement et réponse aux réclamations.
- 🎨 **Gestion des activités** : création et organisation d’activités pour les orphelins.

## 🛠️ Technologies utilisées

- **Java** (JDK 17+)
- **JavaFX** pour l'interface graphique
- **Scene Builder** pour la conception UI
- **MySQL** via **XAMPP phpMyAdmin** pour la base de données
- **Maven** pour la gestion de projet
- **JDBC** pour la communication avec la base de données

## 🖥️ Fonctionnalités principales

- Interface graphique moderne et responsive
- Navigation fluide entre les modules
- Système d’authentification sécurisé
- Base de données relationnelle structurée
- QR Code pour accéder aux profils (orphelins et tuteurs)
- Export PDF, statistiques et notifications par email

## 📦 Installation

1. Clonez le dépôt :

```bash
git clone https://github.com/RayanAloui/PIDEV_Desktop_JAVAFX.git
````
2. Importez le projet dans IntelliJ IDEA (ou tout autre IDE compatible Java).

3. Configurez la base de données MySQL via XAMPP et importez le fichier orphan_care.sql.

4. Lancez l’application via Main.java.

📁 Structure du projet

 OrphanCare/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── controllers/
│ │ │ ├── entites/
│ │ │ ├── services/
│ │ │ └── Main.java
│ │ ├── resources/
│ │ │ └── view/
│ └── test/
├── pom.xml
└── README.md

🤝 Contributeurs
- Aloui Ahmed Rayen
- Louay El Amari
- Ben Abdelkader Sami
- Gasmi Riadh
- Malki Youssef
- Belhadej Salah Sarra
