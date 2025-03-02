package reclamations.main;

import reclamations.entities.Reponse;
import reclamations.services.ReponseService;

import java.sql.Date;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Test if ReponseService instance can be retrieved
        ReponseService reponseService = ReponseService.getInstance();
        if (reponseService == null) {
            System.err.println("Erreur : Impossible d'obtenir une instance de ReponseService.");
            return;
        }
        System.out.println("ReponseService instance retrieved successfully.");

        // Test adding a response
        Reponse reponseToAdd = new Reponse(
                "Test response description", // Description
                Date.valueOf(LocalDate.now()), // Date
                "Pending" // Statut
        );

        try {
            // Adding response
            reponseService.ajouter(reponseToAdd);
            System.out.println("Réponse ajoutée avec succès. ID: " + reponseToAdd.getId());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la réponse : " + e.getMessage());
            e.printStackTrace();
        }

        // Test retrieving all responses after adding
        try {
            System.out.println("\nListe des réponses après ajout :");
            reponseService.getall().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réponses : " + e.getMessage());
            e.printStackTrace();
        }

        // Test updating the response
        try {
            if (reponseToAdd.getId() > 0) { // Ensure ID is valid
                reponseToAdd.setDescription("Updated response description");
                reponseToAdd.setStatut("Resolved");

                // Updating the response
                reponseService.modifier(reponseToAdd);
                System.out.println("Réponse mise à jour avec succès.");
            } else {
                System.out.println("Impossible de mettre à jour : ID invalide.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de la réponse : " + e.getMessage());
            e.printStackTrace();
        }

        // Test retrieving all responses after update
        try {
            System.out.println("\nListe des réponses après mise à jour :");
            reponseService.getall().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réponses : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
