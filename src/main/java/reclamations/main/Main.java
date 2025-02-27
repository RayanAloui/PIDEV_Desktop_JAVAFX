package reclamations.main;

import reclamations.entities.Reclamation;
import reclamations.services.ReclamationService;

import java.sql.Date;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // Test if ReclamationService instance can be retrieved
       ReclamationService reclamationService = ReclamationService.getInstance();
        if (reclamationService == null) {
            System.err.println("Erreur : Impossible d'obtenir une instance de ReclamationService.");
            return;
        }
        System.out.println("ReclamationService instance retrieved successfully.");

        // Test adding a reclamation
        Reclamation reclamationToAdd = new Reclamation(
                "test@example.com",
                "Test issue",
                Date.valueOf(LocalDate.now()),
                ""
        );

        try {
            // Adding reclamation
            reclamationService.ajouter(reclamationToAdd);
            System.out.println("Réclamation ajoutée avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }

        // Test retrieving all reclamations after adding
        try {
            System.out.println("\nListe des réclamations après ajout :");
            reclamationService.getall().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            e.printStackTrace();
        }

        // Now, update the reclamation with id 8 (this id can be changed based on what you have in your database)
        try {
            // Fetch the reclamation to update (assuming id 8 exists)
            Reclamation reclamationToUpdate = new Reclamation();
            reclamationToUpdate.setId(8); // Assuming this id is valid in your database
            reclamationToUpdate.setMail("updated_email@example.com");
            reclamationToUpdate.setDescription("Updated issue description");
            reclamationToUpdate.setDate(Date.valueOf(LocalDate.now()));
            reclamationToUpdate.setStatut("Resolved");

            // Use the 'modifier' method to update the reclamation
            reclamationService.modifier(reclamationToUpdate);
            System.out.println("Réclamation mise à jour avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }

        // Test retrieving all reclamations after update
        try {
            System.out.println("\nListe des réclamations après mise à jour :");
            reclamationService.getall().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
