package visites.main;

import visites.entities.visiteurs;
import visites.entities.visites;
import visites.services.VisiteursService;
import visites.services.VisitesService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Obtenir les instances des services
        VisiteursService visiteursService = VisiteursService.getInstance();
        VisitesService visitesService = VisitesService.getInstance();

        // 1. Ajouter un visiteur
        visiteurs visiteur = new visiteurs("Sami", "BZN", "sami.benabdelkader@gmail.Com", 12345678, "rue du chinois");
        visiteursService.ajouter(visiteur);
        System.out.println("Visiteur ajouté : " + visiteur);

        // 2. Récupérer et afficher tous les visiteurs
        List<visiteurs> visiteursList = visiteursService.getall();
        System.out.println("\nListe des visiteurs après ajout :");
        for (visiteurs v : visiteursList) {
            System.out.println(v);
        }

        // 3. Ajouter une visite
        LocalDate dateVisite = LocalDate.of(2025, 3, 1);
        LocalTime heureVisite = LocalTime.of(14, 30);
        visites visite = new visites(dateVisite, heureVisite, "Consultation", "confirme");
        visite.setId_visiteur(visiteur.getId());
        visitesService.ajouter(visite);
        System.out.println("\nVisite ajoutée : " + visite);

        // 4. Récupérer et afficher toutes les visites
        List<visites> visitesList = visitesService.getall();
        System.out.println("\nListe des visites après ajout :");
        for (visites v : visitesList) {
            System.out.println(v);
        }

        // 5. Modifier le visiteur
        visiteur.setNom("chichi");
        visiteur.setPrenom("ninho");
        visiteursService.modifier(visiteur);
        System.out.println("\nVisiteur après modification : " + visiteursService.getone(visiteur.getId()));

        // 6. Modifier la visite
        visite.setMotif("Entretien");
        visite.setStatut("attente");
        visitesService.modifier(visite);
        System.out.println("\nVisite après modification : " + visitesService.getone(visite.getId_visiteur()));

        // 7. Supprimer la visite
        visitesService.supprimer(visite.getId());
        System.out.println("\nListe des visites après suppression :");
        visitesList = visitesService.getall();
        for (visites v : visitesList) {
            System.out.println(v);
        }

        // 8. Supprimer le visiteur
        visiteursService.supprimer(visiteur.getId());
        System.out.println("\nListe des visiteurs après suppression :");
        visiteursList = visiteursService.getall();
        for (visiteurs v : visiteursList) {
            System.out.println(v);
        }
    }
}
