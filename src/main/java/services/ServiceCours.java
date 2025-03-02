package services;
import entities.Cours;
import main.databaseconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCours implements ICoursService {
    private Connection cnx = databaseconnection.getConnection();

    @Override
    public void ajouterCours(Cours cours) {
        String req = "INSERT INTO cours (titre, contenu, imageC, id_t) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getContenu());
            ps.setString(3, cours.getImageC());
            ps.setInt(4, cours.getIdTuteur());
            ps.executeUpdate();
            System.out.println("Cours ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur d'ajout : " + e.getMessage());
        }
    }

    @Override
    public void modifierCours(Cours cours) {
        String req = "UPDATE cours SET titre=?, contenu=?, imageC=?, id_t=? WHERE idC=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getContenu());
            ps.setString(3, cours.getImageC());
            ps.setInt(4, cours.getIdTuteur());
            ps.setInt(5, cours.getIdC());
            ps.executeUpdate();
            System.out.println("Cours modifié !");
        } catch (SQLException e) {
            System.out.println("Erreur de modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimerCours(int idC) {
        String req = "DELETE FROM cours WHERE idC=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idC);
            ps.executeUpdate();
            System.out.println("Cours supprimé !");
        } catch (SQLException e) {
            System.out.println("Erreur de suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Cours> afficherCours() {
        List<Cours> list = new ArrayList<>();
        String req = "SELECT * FROM cours";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                list.add(new Cours(
                        rs.getInt("idC"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("imageC"),
                        rs.getInt("id_t")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur d'affichage : " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Cours> afficherCoursParTuteur(int idTuteur) {
        List<Cours> list = new ArrayList<>();
        String req = "SELECT * FROM cours WHERE id_t=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idTuteur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Cours(
                        rs.getInt("idC"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("imageC"),
                        rs.getInt("id_t")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur d'affichage : " + e.getMessage());
        }
        return list;
    }

}

