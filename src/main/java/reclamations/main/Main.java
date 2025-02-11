package reclamations.main;

import reclamations.entities.reclamations;
import reclamations.entities.reponses;
import reclamations.services.reclamationsService;
import reclamations.services.reponsesService;

import java.sql.Date;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        //  Initialisation correcte des services
        reclamationsService reclamationsService = reclamationsService.getInstance();  // Corrected
        reponsesService reponsesService = reponsesService.getInstance();

        // Ajouter une réclamation
        reclamations reclamation = new reclamations(
                "user@example.com",
                "Issue with product delivery",
                Date.valueOf(LocalDate.now()),
                "Pending"
        );
        reclamationsService.ajouter(reclamation);

        //  Vérifier si l'ID de la réclamation a bien été généré
        int reclamationId = reclamation.getId();
        if (reclamationId == 0) {
            System.err.println("Erreur : Impossible de récupérer l'ID de la réclamation.");
            return;
        }
        System.out.println("Réclamation ajoutée avec ID: " + reclamationId);

        //  Ajouter une réponse
        reponses reponse = new reponses(
                "We are looking into your issue.",
                Date.valueOf(LocalDate.now()),
                "Pending"
        );

        // ⚠ Ajouter l'ID de la réclamation à la réponse (si `setId_reclamation` existe)
        // reponse.setId_reclamation(reclamationId);

        reponsesService.ajouter(reponse);

        //  Afficher toutes les réclamations
        System.out.println("\nToutes les réclamations :");
        reclamationsService.getall().forEach(System.out::println);

        // Afficher toutes les réponses
        System.out.println("\nToutes les réponses :");
        reponsesService.getall().forEach(System.out::println);
    }
}
