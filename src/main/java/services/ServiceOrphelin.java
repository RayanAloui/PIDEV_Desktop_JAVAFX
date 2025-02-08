package services;

import entities.Orphelin;

import java.sql.*;
import java.util.ArrayList;
import main.databaseconnection;

public class ServiceOrphelin implements IOrphelinService {

    @Override
    public void ajouter(Orphelin orphelin) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "INSERT INTO orphelins (nomO, prenomO, dateNaissance, sexe, situationScolaire, idTuteur) VALUES (?,?,?,?,?,?)";
            pst = conn.prepareStatement(query);
            pst.setString(1, orphelin.getNomO());
            pst.setString(2, orphelin.getPrenomO());
            pst.setString(3, orphelin.getDateNaissance());
            pst.setString(4, orphelin.getSexe());
            pst.setString(5, orphelin.getSituationScolaire());
            pst.setInt(6, orphelin.getIdTuteur());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Orphelin ajouté avec succès.");
            } else {
                System.out.println("❌ L'ajout d'orphelin a échoué.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout d'orphelin.");

        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public void updateOrphelin(Orphelin orphelin) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "UPDATE orphelins SET nomO = ?, prenomO = ?, dateNaissance = ?, sexe = ?, situationScolaire = ?, idTuteur = ? WHERE idO = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, orphelin.getNomO());
            pst.setString(2, orphelin.getPrenomO());
            pst.setString(3, orphelin.getDateNaissance());
            pst.setString(4, orphelin.getSexe());
            pst.setString(5, orphelin.getSituationScolaire());
            pst.setInt(6, orphelin.getIdTuteur());
            pst.setInt(7, orphelin.getIdO());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Orphelin mis à jour avec succès.");
            } else {
                System.out.println("❌ La mise à jour de l'orphelin a échoué.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la mise à jour de l'orphelin.");
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
            String query = "DELETE FROM orphelins WHERE idO = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Orphelin supprimé avec succès.");
            } else {
                System.out.println("❌ La suppression de l'orphelin a échoué.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la suppression de l'orphelin.");
        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public ArrayList<Orphelin> afficherAll() throws SQLException {
        ArrayList<Orphelin> orphelins = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT * FROM orphelins";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Orphelin orphelin = new Orphelin(
                        rs.getInt("idO"),
                        rs.getString("nomO"),
                        rs.getString("prenomO"),
                        rs.getString("dateNaissance"),
                        rs.getString("sexe"),
                        rs.getString("situationScolaire"),
                        rs.getInt("idTuteur")
                );
                orphelins.add(orphelin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'affichage des orphelins.");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return orphelins;
    }
}

