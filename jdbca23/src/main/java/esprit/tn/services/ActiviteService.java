package esprit.tn.services;

import esprit.tn.entities.Activite;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActiviteService implements Iservice0<Activite> {
    Connection cnx;

    public ActiviteService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Activite activite) {
        String req = "INSERT INTO activite (nom, categorie, duree, nom_du_tuteur, date_activite, lieu, description, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, activite.getNom());
            stm.setString(2, activite.getCategorie());
            stm.setString(3, activite.getDuree());
            stm.setString(4, activite.getNom_du_tuteur());
            stm.setString(5, activite.getDate_activite());
            stm.setString(6, activite.getLieu());
            stm.setString(7, activite.getDescription());
            stm.setString(8, activite.getStatut());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Activite activite, int id) {
        String req = "UPDATE activite SET nom=?, categorie=?, duree=?, nom_du_tuteur=?, date_activite=?, lieu=?, description=?, statut=? WHERE id=?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, activite.getNom());
            stm.setString(2, activite.getCategorie());
            stm.setString(3, activite.getDuree());
            stm.setString(4, activite.getNom_du_tuteur());
            stm.setString(5, activite.getDate_activite());
            stm.setString(6, activite.getLieu());
            stm.setString(7, activite.getDescription());
            stm.setString(8, activite.getStatut());
            stm.setInt(9, id);

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM activite WHERE id=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Activite> getall() {
        List<Activite> activites = new ArrayList<>();
        String req = "SELECT * FROM activite"; // Query to fetch all activities

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Activite activite = new Activite(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getString("duree"),
                        rs.getString("nom_du_tuteur"),
                        rs.getString("date_activite"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getString("statut")
                );
                activites.add(activite);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return activites;
    }

    @Override
    public Activite getone(int id) {
        String req = "SELECT * FROM activite WHERE id=?";
        Activite activite = null;

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                activite = new Activite(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getString("duree"),
                        rs.getString("nom_du_tuteur"),
                        rs.getString("date_activite"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getString("statut")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return activite;
    }

    // Méthode pour rechercher des activités par leur nom
    public List<Activite> searchByNom(String nomRecherche) {
        List<Activite> activites = new ArrayList<>();
        String req = "SELECT * FROM activite WHERE nom LIKE ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, "%" + nomRecherche + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Activite activite = new Activite(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getString("duree"),
                        rs.getString("nom_du_tuteur"),
                        rs.getString("date_activite"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getString("statut")
                );
                activites.add(activite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activites;
    }

    // Méthode pour filtrer les activités par catégorie
    public List<Activite> getActivitesByCategorie(String categorie) {
        List<Activite> activites = new ArrayList<>();
        String req = "SELECT * FROM activite WHERE categorie = ?";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, categorie);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Activite activite = new Activite(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getString("duree"),
                        rs.getString("nom_du_tuteur"),
                        rs.getString("date_activite"),
                        rs.getString("lieu"),
                        rs.getString("description"),
                        rs.getString("statut")
                );
                activites.add(activite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activites;
    }

    // Méthode pour récupérer toutes les activités pour l'exportation
    public List<Activite> getAllActivitesForExport() {
        return getall(); // Cette méthode renvoie toutes les activités
    }
}

