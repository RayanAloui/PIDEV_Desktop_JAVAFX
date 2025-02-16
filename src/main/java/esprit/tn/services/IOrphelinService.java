package esprit.tn.services;

import esprit.tn.entities.Orphelin;

import java.sql.SQLException;
import java.util.ArrayList;


public interface IOrphelinService {

    void ajouter(Orphelin orphelin) throws SQLException;
    void updateOrphelin(Orphelin orphelin) throws SQLException;
    void delete(int id) throws SQLException;
    ArrayList<Orphelin> afficherAll() throws SQLException;

}
