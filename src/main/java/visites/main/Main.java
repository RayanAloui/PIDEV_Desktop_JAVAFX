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
        // Utilisation de getInstance() pour obtenir les instances
        VisiteursService visiteursService = VisiteursService.getInstance();
        VisitesService visitesService = VisitesService.getInstance();

        // Ajouter un visiteur
        visiteurs visiteur = new visiteurs("azizos", "chafcha", "ziizi.dupont@email.com", 12345678, "123 Rue de Paris");
        visiteursService.ajouter(visiteur);

        // Ajouter une visite
        LocalDate dateVisite = LocalDate.of(2025, 3, 1);  // 1er mars 2025
        LocalTime heureVisite = LocalTime.of(14, 30);
        visites visite = new visites(dateVisite, heureVisite, "Consultation", "Confirmée");
        visite.setId_visiteur(visiteur.getId());
        visitesService.ajouter(visite);


    }
}
