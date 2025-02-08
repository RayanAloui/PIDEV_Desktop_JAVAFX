package main;/*public class main {
    public static void main(String[] args) {
        Connection conn = databaseconnection.getConnection();
        if (conn != null) {
            System.out.println("✅ Base de données connectée avec succès !");
        } else {
            System.out.println("❌ Problème de connexion.");
        }
    }
}*/

import services.ServiceTuteur;
import entities.Tuteur;
import java.sql.SQLException;
import java.util.ArrayList;

/*public class main {
    public static void main(String[] args) {
        ServiceTuteur serviceTuteur = new ServiceTuteur();

        Tuteur t = new Tuteur("12345678", "aloui", "rayen", "99058580", "Rue des Roses");

        try {
            // Ajouter un tuteur
            serviceTuteur.ajouter(t);

            // Mettre à jour un tuteur (exemple)
            t.setNomT("Dupont Modifié");
            serviceTuteur.updateTuteur(t);

            // Afficher tous les tuteurs
            ArrayList<Tuteur> tuteurs = serviceTuteur.afficherAll();
            tuteurs.forEach(tuteur -> System.out.println(tuteur));

            // Supprimer un tuteur
            serviceTuteur.delete(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}*/

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServiceTuteur serviceTuteur = new ServiceTuteur();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== MENU GESTION DES TUTEURS ====");
            System.out.println("1️⃣  Ajouter un tuteur");
            System.out.println("2️⃣  Modifier un tuteur");
            System.out.println("3️⃣  Supprimer un tuteur");
            System.out.println("4️⃣  Afficher tous les tuteurs");
            System.out.println("5️⃣  Quitter");
            System.out.print("👉 Choisissez une option : ");

            int choix = scanner.nextInt();
            scanner.nextLine();  // Pour consommer le retour à la ligne

            switch (choix) {
                case 1:
                    ajouterTuteur(serviceTuteur, scanner);
                    break;
                case 2:
                    modifierTuteur(serviceTuteur, scanner);
                    break;
                case 3:
                    supprimerTuteur(serviceTuteur, scanner);
                    break;
                case 4:
                    afficherTuteurs(serviceTuteur);
                    break;
                case 5:
                    System.out.println("👋 Programme terminé !");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
    }

    // ✅ Fonction pour ajouter un tuteur
    private static void ajouterTuteur(ServiceTuteur serviceTuteur, Scanner scanner) {
        System.out.print("📄 Saisissez le CIN : ");
        String cin = scanner.nextLine();

        System.out.print("📄 Saisissez le nom : ");
        String nom = scanner.nextLine();

        System.out.print("📄 Saisissez le prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("📄 Saisissez le téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("📄 Saisissez l'adresse : ");
        String adresse = scanner.nextLine();

        Tuteur tuteur = new Tuteur(cin, nom, prenom, telephone, adresse);

        try {
            serviceTuteur.ajouter(tuteur);
            System.out.println("✅ Tuteur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du tuteur.");
            e.printStackTrace();
        }
    }

    // ✅ Fonction pour modifier un tuteur
    private static void modifierTuteur(ServiceTuteur serviceTuteur, Scanner scanner) {
        System.out.print("🔄 Entrez l'ID du tuteur à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("📄 Nouveau cin : ");
        String cin = scanner.nextLine();

        System.out.print("📄 Nouveau nom : ");
        String nom = scanner.nextLine();

        System.out.print("📄 Nouveau prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("📄 Nouveau téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("📄 Nouvelle adresse : ");
        String adresse = scanner.nextLine();

        Tuteur tuteur = new Tuteur(id, cin , nom, prenom, telephone, adresse);

        try {
            serviceTuteur.updateTuteur(tuteur);
            System.out.println("✅ Tuteur modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification du tuteur.");
            e.printStackTrace();
        }
    }

    // ✅ Fonction pour supprimer un tuteur
    private static void supprimerTuteur(ServiceTuteur serviceTuteur, Scanner scanner) {
        System.out.print("🗑️ Entrez l'ID du tuteur à supprimer : ");
        int id = scanner.nextInt();

        try {
            serviceTuteur.delete(id);
            System.out.println("✅ Tuteur supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression du tuteur.");
            e.printStackTrace();
        }
    }

    // ✅ Fonction pour afficher tous les tuteurs
    private static void afficherTuteurs(ServiceTuteur serviceTuteur) {
        try {
            ArrayList<Tuteur> tuteurs = serviceTuteur.afficherAll();
            if (tuteurs.isEmpty()) {
                System.out.println("📭 Aucun tuteur trouvé.");
            } else {
                System.out.println("\n📋 Liste des tuteurs :");
                tuteurs.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'affichage des tuteurs.");
            e.printStackTrace();
        }
    }
}



