package main;

/*public class main {
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
import services.ServiceOrphelin;
import entities.Tuteur;
import entities.Orphelin;
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
        ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== MENU GESTION ====");
            System.out.println("1️⃣  Gestion des tuteurs");
            System.out.println("2️⃣  Gestion des orphelins");
            System.out.println("3️⃣  Quitter");
            System.out.print("👉 Choisissez une option : ");

            int choixPrincipal = scanner.nextInt();
            scanner.nextLine();  // Consommer le retour à la ligne

            switch (choixPrincipal) {
                case 1:
                    menuTuteur(serviceTuteur, scanner);
                    break;
                case 2:
                    menuOrphelin(serviceOrphelin, scanner);
                    break;
                case 3:
                    System.out.println("👋 Programme terminé !");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
    }

    // ✅ Sous-menu pour la gestion des tuteurs
    private static void menuTuteur(ServiceTuteur serviceTuteur, Scanner scanner) {
        while (true) {
            System.out.println("\n==== GESTION DES TUTEURS ====");
            System.out.println("1️⃣  Ajouter un tuteur");
            System.out.println("2️⃣  Modifier un tuteur");
            System.out.println("3️⃣  Supprimer un tuteur");
            System.out.println("4️⃣  Afficher tous les tuteurs");
            System.out.println("5️⃣  Retour au menu principal");
            System.out.print("👉 Choisissez une option : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

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
                    return;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
    }

    // ✅ Sous-menu pour la gestion des orphelins
    private static void menuOrphelin(ServiceOrphelin serviceOrphelin, Scanner scanner) {
        while (true) {
            System.out.println("\n==== GESTION DES ORPHELINS ====");
            System.out.println("1️⃣  Ajouter un orphelin");
            System.out.println("2️⃣  Modifier un orphelin");
            System.out.println("3️⃣  Supprimer un orphelin");
            System.out.println("4️⃣  Afficher tous les orphelins");
            System.out.println("5️⃣  Retour au menu principal");
            System.out.print("👉 Choisissez une option : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterOrphelin(serviceOrphelin, scanner);
                    break;
                case 2:
                    modifierOrphelin(serviceOrphelin, scanner);
                    break;
                case 3:
                    supprimerOrphelin(serviceOrphelin, scanner);
                    break;
                case 4:
                    afficherOrphelins(serviceOrphelin);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("❌ Option invalide. Veuillez réessayer.");
            }
        }
    }

    private static void ajouterOrphelin(ServiceOrphelin serviceOrphelin, Scanner scanner) {
        System.out.print("📄 Saisissez le nom : ");
        String nom = scanner.nextLine();

        System.out.print("📄 Saisissez le prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("📄 Saisissez la date de naissance (YYYY-MM-DD) : ");
        String dateNaissance = scanner.nextLine();

        System.out.print("📄 Saisissez le sexe (M/F) : ");
        String sexe = scanner.nextLine();

        System.out.print("📄 Saisissez la situation scolaire : ");
        String situationScolaire = scanner.nextLine();

        System.out.print("📄 Saisissez l'ID du tuteur : ");
        int idTuteur = scanner.nextInt();
        scanner.nextLine();

        Orphelin orphelin = new Orphelin(nom, prenom, dateNaissance, sexe, situationScolaire, idTuteur);

        try {
            serviceOrphelin.ajouter(orphelin);
            System.out.println("✅ Orphelin ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'orphelin.");
            e.printStackTrace();
        }
    }

    private static void modifierOrphelin(ServiceOrphelin serviceOrphelin, Scanner scanner) {
        System.out.print("🔄 Entrez l'ID d'orphelin à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("📄 Saisissez le nom : ");
        String nom = scanner.nextLine();

        System.out.print("📄 Saisissez le prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("📄 Saisissez la date de naissance (YYYY-MM-DD) : ");
        String dateNaissance = scanner.nextLine();

        System.out.print("📄 Saisissez le sexe (M/F) : ");
        String sexe = scanner.nextLine();

        System.out.print("📄 Saisissez la situation scolaire : ");
        String situationScolaire = scanner.nextLine();

        System.out.print("📄 Saisissez l'ID du tuteur : ");
        int idTuteur = scanner.nextInt();

        Orphelin orphelin = new Orphelin(id,nom, prenom,dateNaissance,sexe,situationScolaire,idTuteur);

        try {
            serviceOrphelin.updateOrphelin(orphelin);
            System.out.println("✅ Orphelin modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification d'orphelin.");
            e.printStackTrace();
        }
    }

    private static void supprimerOrphelin(ServiceOrphelin serviceOrphelin, Scanner scanner) {
        System.out.print("🗑️ Entrez l'ID d'orphelin à supprimer : ");
        int id = scanner.nextInt();

        try {
            serviceOrphelin.delete(id);
            System.out.println("✅ Orphelin supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression d'orphelin.");
            e.printStackTrace();
        }
    }

    private static void afficherOrphelins(ServiceOrphelin serviceOrphelin) {
        try {
            ArrayList<Orphelin> orphelins = serviceOrphelin.afficherAll();
            if (orphelins.isEmpty()) {
                System.out.println("📭 Aucun orphelin trouvé.");
            } else {
                System.out.println("\n📋 Liste des orphelins :");
                orphelins.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'affichage des orphelins.");
            e.printStackTrace();
        }
    }

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



