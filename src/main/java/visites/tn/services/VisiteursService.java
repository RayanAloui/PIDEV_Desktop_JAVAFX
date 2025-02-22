package visites.tn.services;

import visites.tn.entities.visiteurs;
import visites.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisiteursService implements Iservices<visiteurs> {

    Connection cnx;
    static VisiteursService instance;

    public VisiteursService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public static VisiteursService getInstance() {
        if (instance == null) {
            instance = new VisiteursService();
        }
        return instance;
    }

    @Override
    public void ajouter(visiteurs visiteur) throws SQLException {
        String req = "INSERT INTO visiteurs (nom, prenom, email, tel, adresse, cin) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, visiteur.getNom());
            stm.setString(2, visiteur.getPrenom());
            stm.setString(3, visiteur.getEmail());
            stm.setInt(4, visiteur.getTel());
            stm.setString(5, visiteur.getAdresse());
            stm.setString(6, visiteur.getCin());  // 🔹 Correction ici (String au lieu d'int)
            stm.executeUpdate();

            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    visiteur.setId(generatedKeys.getInt(1));
                    System.out.println("Visiteur ajouté : " + visiteur);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du visiteur : " + e.getMessage());
        }
    }

    @Override
    public void modifier(visiteurs visiteur) {
        try {
            if (!visiteur.getCin().matches("\\d{8}")) { // 🔹 Vérifie que CIN contient exactement 8 chiffres
                throw new IllegalArgumentException("Erreur : Le CIN doit contenir exactement 8 chiffres.");
            }

            String req = "UPDATE visiteurs SET nom = ?, prenom = ?, email = ?, tel = ?, adresse = ?, cin = ? WHERE id = ?";

            try (PreparedStatement stm = cnx.prepareStatement(req)) {
                stm.setString(1, visiteur.getNom());
                stm.setString(2, visiteur.getPrenom());
                stm.setString(3, visiteur.getEmail());
                stm.setInt(4, visiteur.getTel());
                stm.setString(5, visiteur.getAdresse());
                stm.setString(6, visiteur.getCin());  // 🔹 Correction ici
                stm.setInt(7, visiteur.getId());
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
                v.setCin(rs.getString("cin"));  // 🔹 Correction ici
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
                    v.setCin(rs.getString("cin")); // 🔹 Correction ici
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du visiteur : " + e.getMessage());
        }

        return v;
    }

    public visiteurs rechercherParCIN(String cin) {
        String req = "SELECT * FROM visiteurs WHERE cin = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, cin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new visiteurs(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getInt("tel"),
                        rs.getString("adresse"),
                        rs.getString("cin") // 🔹 Correction ici
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du visiteur par CIN : " + e.getMessage());
        }
        return null; // 🔹 Aucun visiteur trouvé
    }
}
