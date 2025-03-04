package esprit.tn.services;

import esprit.tn.entities.Cours;
import java.util.List;

public interface ICoursService {
    void ajouterCours(Cours cours);
    void modifierCours(Cours cours);
    void supprimerCours(int idC);
    List<Cours> afficherCours();
    List<Cours> afficherCoursParTuteur(int idTuteur);
}

