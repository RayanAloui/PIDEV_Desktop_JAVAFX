package services;

import entities.Orphelin;

import java.sql.*;
import java.util.ArrayList;
import main.databaseconnection;
import java.util.regex.Pattern;

public class ServiceOrphelin implements IOrphelinService {

    @Override
    public void ajouter(Orphelin orphelin) throws SQLException {

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", orphelin.getNomO())) {
            throw new IllegalArgumentException("Le nom ne doit contenir que des lettres et des espaces.");
        }

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", orphelin.getPrenomO())) {
            throw new IllegalArgumentException("Le prénom ne doit contenir que des lettres et des espaces.");
        }

        if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", orphelin.getDateNaissance())) {
            throw new IllegalArgumentException("La date de naissance doit être au format YYYY-MM-DD.");
        }

        if (!orphelin.getSexe().equalsIgnoreCase("M") && !orphelin.getSexe().equalsIgnoreCase("F")) {
            throw new IllegalArgumentException("Le sexe doit être 'M' ou 'F'.");
        }

        if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", orphelin.getSituationScolaire())) {
            throw new IllegalArgumentException("La situation scolaire ne doit contenir que des lettres et des espaces.");
        }

        if (orphelin.getIdTuteur() <= 0 || !tuteurExiste(orphelin.getIdTuteur())) {
            throw new IllegalArgumentException("L'ID du tuteur doit être un entier positif existant dans la base.");
        }

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
                System.out.println("Orphelin ajouté avec succès.");
            } else {
                System.out.println("L'ajout d'orphelin a échoué.");
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

            // Vérifications des nouvelles valeurs AVANT de les appliquer
            if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", orphelin.getNomO())) {
                throw new IllegalArgumentException("Le nom ne doit contenir que des lettres et des espaces.");
            }

            if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", orphelin.getPrenomO())) {
                throw new IllegalArgumentException("Le prénom ne doit contenir que des lettres et des espaces.");
            }

            if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", orphelin.getDateNaissance())) {
                throw new IllegalArgumentException("La date de naissance doit être au format YYYY-MM-DD.");
            }

            if (!orphelin.getSexe().equalsIgnoreCase("M") && !orphelin.getSexe().equalsIgnoreCase("F")) {
                throw new IllegalArgumentException("Le sexe doit être 'M' ou 'F'.");
            }

            if (!Pattern.matches("^[a-zA-ZÀ-ÿ\\s]+$", orphelin.getSituationScolaire())) {
                throw new IllegalArgumentException("La situation scolaire ne doit contenir que des lettres et des espaces.");
            }

            if (orphelin.getIdTuteur() <= 0 || !tuteurExiste(orphelin.getIdTuteur())) {
                throw new IllegalArgumentException("L'ID du tuteur doit être un entier positif existant dans la base.");
            }

            // Connexion et mise à jour
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
                System.out.println("Orphelin mis à jour avec succès !");
            } else {
                System.out.println("La mise à jour a échoué.");
            }

        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }


    @Override
    public void delete(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être un entier positif.");
        }

        if (!orphelinExiste(id)) {
            throw new IllegalArgumentException("L'ID donné ne correspond à aucun orphelin existant.");
        }

        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "DELETE FROM orphelins WHERE idO = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Orphelin supprimé avec succès.");
            } else {
                System.out.println("La suppression de l'orphelin a échoué.");
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
                        //rs.getInt("idO"),
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

    private boolean tuteurExiste(int idTuteur) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT COUNT(*) FROM tuteurs WHERE idT= ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, idTuteur);
            rs = pst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } finally {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
        return exists;
    }

    private boolean orphelinExiste(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT COUNT(*) FROM orphelins WHERE idO = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } finally {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
        return exists;
    }

    public Orphelin getOrphelinById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT * FROM orphelins WHERE idO = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                return new Orphelin(
                        rs.getInt("idO"),
                        rs.getString("nomO"),
                        rs.getString("prenomO"),
                        rs.getString("dateNaissance"),
                        rs.getString("sexe"),
                        rs.getString("situationScolaire"),
                        rs.getInt("idTuteur")
                );
            } else {
                return null; // Aucun orphelin trouvé
            }
        } finally {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

}

