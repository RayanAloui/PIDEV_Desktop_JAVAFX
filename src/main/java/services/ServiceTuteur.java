package services;

import entities.Tuteur;

import java.sql.*;
import java.util.ArrayList;
import main.databaseconnection;

public class ServiceTuteur implements ITuteurService {

    @Override
    public void ajouter(Tuteur t) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "INSERT INTO tuteurs (cinT, nomT, prenomT, telephoneT, adresseT) VALUES (?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, t.getCinT());
            pst.setString(2, t.getNomT());
            pst.setString(3, t.getPrenomT());
            pst.setString(4, t.getTelephoneT());
            pst.setString(5, t.getAdresseT());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Tuteur ajouté avec succès.");
            } else {
                System.out.println("❌ L'ajout du tuteur a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout du tuteur.");
        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public void updateTuteur(Tuteur t) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "UPDATE tuteurs SET cinT = ?, nomT = ?, prenomT = ?, telephoneT = ?, adresseT = ? WHERE idT = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, t.getCinT());
            pst.setString(2, t.getNomT());
            pst.setString(3, t.getPrenomT());
            pst.setString(4, t.getTelephoneT());
            pst.setString(5, t.getAdresseT());
            pst.setInt(6, t.getIdT());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Tuteur mis à jour avec succès.");
            } else {
                System.out.println("❌ La mise à jour du tuteur a échoué. ID introuvable.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la mise à jour du tuteur.");
        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "DELETE FROM tuteurs WHERE idT = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Tuteur supprimé avec succès.");
            } else {
                System.out.println("❌ La suppression du tuteur a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la suppression du tuteur.");
        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public ArrayList<Tuteur> afficherAll() throws SQLException {
        ArrayList<Tuteur> tuteurs = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT * FROM tuteurs";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("idT");  // Récupération de l'ID
                String cin = rs.getString("cinT");
                String nom = rs.getString("nomT");
                String prenom = rs.getString("prenomT");
                String telephone = rs.getString("telephoneT");
                String adresse = rs.getString("adresseT");

                Tuteur tuteur = new Tuteur(id, cin, nom, prenom, telephone, adresse);
                tuteurs.add(tuteur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération des tuteurs.");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return tuteurs;
    }

}

