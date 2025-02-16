package esprit.tn.services;


import esprit.tn.entities.Tuteur;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ITuteurService {
    void ajouter(Tuteur t) throws SQLException;
    void updateTuteur(Tuteur t) throws SQLException;
    void delete(int id) throws SQLException;
    ArrayList<Tuteur> afficherAll() throws SQLException;
}
