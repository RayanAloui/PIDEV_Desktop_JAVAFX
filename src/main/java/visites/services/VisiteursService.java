package visites.services;

import visites.entities.visiteurs;
import visites.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisiteursService implements Iservices<visiteurs> {



    Connection cnx;
    static VisiteursService instance;

    // Constructeur public
    public VisiteursService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Méthode pour obtenir l'instance unique de VisiteursService
    public static VisiteursService getInstance() {
        if (instance == null) {
            instance = new VisiteursService();
        }
        return instance;
    }

    @Override
    public void ajouter(visiteurs visiteur) {
        try {
            if (visiteur.getNom() == null || !visiteur.getNom().matches("[A-Za-z]+") ||
                    visiteur.getPrenom() == null || !visiteur.getPrenom().matches("[A-Za-z]+") ||
                    visiteur.getEmail() == null || !visiteur.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") ||
                    String.valueOf(visiteur.getTel()).length() != 8 ||
                    visiteur.getAdresse() == null || visiteur.getAdresse().trim().isEmpty()) {
                throw new IllegalArgumentException("Erreur : Données invalides pour le visiteur.");
            }

            String req = "INSERT INTO visiteurs (nom, prenom, email, tel, adresse) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, visiteur.getNom());
                stm.setString(2, visiteur.getPrenom());
                stm.setString(3, visiteur.getEmail());
                stm.setInt(4, visiteur.getTel());
                stm.setString(5, visiteur.getAdresse());
                stm.executeUpdate();

                // Récupérer l'ID généré
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        visiteur.setId(generatedKeys.getInt(1));  // Met à jour l'id du visiteur avec l'ID généré
                        System.out.println("Visiteur ajouté : " + visiteur);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ajout du visiteur : " + e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(visiteurs visiteur) {
        try {
            if (visiteur.getNom() == null || !visiteur.getNom().matches("[A-Za-z]+") ||
                    visiteur.getPrenom() == null || !visiteur.getPrenom().matches("[A-Za-z]+") ||
                    visiteur.getEmail() == null || !visiteur.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") ||
                    String.valueOf(visiteur.getTel()).length() != 8 ||
                    visiteur.getAdresse() == null || visiteur.getAdresse().trim().isEmpty()) {
                throw new IllegalArgumentException("Erreur : Données invalides pour le visiteur.");
            }

            String req = "UPDATE visiteurs SET nom = ?, prenom = ?, email = ?, tel = ?, adresse = ? WHERE id = ?";

            try (PreparedStatement stm = cnx.prepareStatement(req)) {
                stm.setString(1, visiteur.getNom());
                stm.setString(2, visiteur.getPrenom());
                stm.setString(3, visiteur.getEmail());
                stm.setInt(4, visiteur.getTel());
                stm.setString(5, visiteur.getAdresse());
                stm.setInt(6, visiteur.getId());
                stm.executeUpdate();
                System.out.println("Visiteur modifié : " + visiteur);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la mise à jour du visiteur : " + e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM visiteurs WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du visiteur : " + e.getMessage());
        }
    }

    @Override
    public List<visiteurs> getall() {
        List<visiteurs> visiteursList = new ArrayList<>();
        String req = "SELECT * FROM visiteurs";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                visiteurs v = new visiteurs();
                v.setId(rs.getInt("id"));
                v.setNom(rs.getString("nom"));
                v.setPrenom(rs.getString("prenom"));
                v.setEmail(rs.getString("email"));
                v.setTel(rs.getInt("tel"));
                v.setAdresse(rs.getString("adresse"));
                visiteursList.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visiteurs : " + e.getMessage());
        }

        return visiteursList;
    }

    @Override
    public visiteurs getone(int id) {
        visiteurs v = null;
        String req = "SELECT * FROM visiteurs WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    v = new visiteurs();
                    v.setId(rs.getInt("id"));
                    v.setNom(rs.getString("nom"));
                    v.setPrenom(rs.getString("prenom"));
                    v.setEmail(rs.getString("email"));
                    v.setTel(rs.getInt("tel"));
                    v.setAdresse(rs.getString("adresse"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du visiteur : " + e.getMessage());
        }

        return v;
    }
}