package reclamations.main;


import reclamations.entities.Reclamation;
import reclamations.entities.Reponse;
import reclamations.services.ReclamationService;
import reclamations.services.ReponseService;

import java.sql.Date;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Corrected initialization of services
        ReclamationService reclamationService = ReclamationService.getInstance();
        ReponseService reponseService = ReponseService.getInstance();

        // Ajouter une réclamation
        Reclamation reclamation = new Reclamation(
                "user@example.com",
                "Issue with product delivery",
                Date.valueOf(LocalDate.now()),
                "Pending"
        );
        reclamationService.ajouter(reclamation);

        // Vérifier si l'ID de la réclamation a bien été généré
        int reclamationId = reclamation.getId();
        if (reclamationId == 0) {
            System.err.println("Erreur : Impossible de récupérer l'ID de la réclamation.");
            return;
        }
        System.out.println("Réclamation ajoutée avec ID: " + reclamationId);

        // Ajouter une réponse
        Reponse reponse = new Reponse(
                "We are looking into your issue.",
                Date.valueOf(LocalDate.now()),
                "Pending"
        );

        // ⚠ Ajouter l'ID de la réclamation à la réponse (si setId_reclamation existe)
        // reponse.setId_reclamation(reclamationId);

        reponseService.ajouter(reponse);

        // Afficher toutes les réclamations
        System.out.println("\nToutes les réclamations :");
        reclamationService.getall().forEach(System.out::println);

        // Afficher toutes les réponses
        System.out.println("\nToutes les réponses :");
        reponseService.getall().forEach(System.out::println);
    }
}