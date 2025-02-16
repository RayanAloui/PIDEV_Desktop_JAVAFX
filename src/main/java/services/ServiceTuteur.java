package services;

import entities.Tuteur;

import java.sql.*;
import java.util.ArrayList;
import main.databaseconnection;
import java.util.List;

public class ServiceTuteur implements ITuteurService {

    private Connection connection;

    public ServiceTuteur() {
        connection = databaseconnection.getConnection();
    }

    @Override
    public void ajouter(Tuteur t) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        if (!t.getCinT().matches("\\d{8}")) {
            throw new IllegalArgumentException("CIN invalide : doit contenir exactement 8 chiffres.");
        }
        if (!t.getNomT().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Nom invalide : doit contenir uniquement des lettres et des espaces.");
        }
        if (!t.getPrenomT().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Prénom invalide : doit contenir uniquement des lettres et des espaces.");
        }
        if (!t.getTelephoneT().matches("\\d{8}")) {
            throw new IllegalArgumentException("Téléphone invalide : doit contenir exactement 8 chiffres.");
        }
        if (t.getAdresseT().trim().isEmpty()) {
            throw new IllegalArgumentException("Adresse invalide : elle ne doit pas être vide.");
        }

        if (cinExiste(t.getCinT())) {
            throw new SQLException("Ce numéro de CIN est déjà utilisé !");
        }
        if (telephoneExiste(t.getTelephoneT())) {
            throw new SQLException("Ce numéro de téléphone est déjà utilisé !");
        }

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
                System.out.println("Tuteur ajouté avec succès.");
            } else {
                System.out.println("L'ajout du tuteur a échoué.");
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
        if (!t.getCinT().matches("\\d{8}")) {
            throw new IllegalArgumentException("CIN invalide : doit contenir exactement 8 chiffres.");
        }
        if (!t.getNomT().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Nom invalide : doit contenir uniquement des lettres et des espaces.");
        }
        if (!t.getPrenomT().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Prénom invalide : doit contenir uniquement des lettres et des espaces.");
        }
        if (!t.getTelephoneT().matches("\\d{8}")) {
            throw new IllegalArgumentException("Téléphone invalide : doit contenir exactement 8 chiffres.");
        }
        if (t.getAdresseT().trim().isEmpty()) {
            throw new IllegalArgumentException("Adresse invalide : elle ne doit pas être vide.");
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = databaseconnection.getConnection();

            // Vérifier si le CIN est déjà utilisé par un autre tuteur
            String checkCINQuery = "SELECT idT FROM tuteurs WHERE cinT = ? AND idT != ?";
            try (PreparedStatement checkCINStmt = conn.prepareStatement(checkCINQuery)) {
                checkCINStmt.setString(1, t.getCinT());
                checkCINStmt.setInt(2, t.getIdT());

                rs = checkCINStmt.executeQuery();
                if (rs.next()) {
                    throw new SQLException("Le CIN " + t.getCinT() + " est déjà utilisé par un autre tuteur !");
                }
            }

            // Vérifier si le numéro de téléphone est déjà utilisé par un autre tuteur
            String checkTelQuery = "SELECT idT FROM tuteurs WHERE telephoneT = ? AND idT != ?";
            try (PreparedStatement checkTelStmt = conn.prepareStatement(checkTelQuery)) {
                checkTelStmt.setString(1, t.getTelephoneT());
                checkTelStmt.setInt(2, t.getIdT());

                rs = checkTelStmt.executeQuery();
                if (rs.next()) {
                    throw new SQLException("Le numéro de téléphone " + t.getTelephoneT() + " est déjà utilisé par un autre tuteur !");
                }
            }

            // Mise à jour du tuteur
            String updateQuery = "UPDATE tuteurs SET cinT = ?, nomT = ?, prenomT = ?, telephoneT = ?, adresseT = ? WHERE idT = ?";
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, t.getCinT());
            pst.setString(2, t.getNomT());
            pst.setString(3, t.getPrenomT());
            pst.setString(4, t.getTelephoneT());
            pst.setString(5, t.getAdresseT());
            pst.setInt(6, t.getIdT());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tuteur mis à jour avec succès.");
            } else {
                throw new SQLException("La mise à jour a échoué. ID introuvable.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la mise à jour du tuteur : " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }



    @Override
    public void delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être un entier positif.");
        }

        if (!tuteurExiste(id)) {
            throw new IllegalArgumentException("L'ID donné ne correspond à aucun tuteur existant.");
        }

        try {
            conn = databaseconnection.getConnection();
            String query = "DELETE FROM tuteurs WHERE idT = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tuteur supprimé avec succès.");
            } else {
                System.out.println("La suppression du tuteur a échoué.");
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
                //int id = rs.getInt("idT");
                String cin = rs.getString("cinT");
                String nom = rs.getString("nomT");
                String prenom = rs.getString("prenomT");
                String telephone = rs.getString("telephoneT");
                String adresse = rs.getString("adresseT");

                Tuteur tuteur = new Tuteur(cin, nom, prenom, telephone, adresse);
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

    public Tuteur getTuteurById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Tuteur tuteur = null;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT * FROM tuteurs WHERE idT = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);

            rs = pst.executeQuery();

            if (rs.next()) {
                // Récupérer les données du résultat de la requête
                String cin = rs.getString("cinT");
                String nom = rs.getString("nomT");
                String prenom = rs.getString("prenomT");
                String telephone = rs.getString("telephoneT");
                String adresse = rs.getString("adresseT");

                // Créer un objet Tuteur avec les données récupérées
                tuteur = new Tuteur(id, cin, nom, prenom, telephone, adresse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération du tuteur.");
        } finally {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }

        return tuteur;
    }

    public List<Tuteur> getAllTuteurs() throws SQLException {
        List<Tuteur> tuteurs = new ArrayList<>();
        String query = "SELECT * FROM tuteurs";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Tuteur tuteur = new Tuteur(
                    rs.getInt("idT"),
                    rs.getString("cinT"),
                    rs.getString("nomT"),
                    rs.getString("prenomT"),
                    rs.getString("telephoneT"),
                    rs.getString("adresseT")
            );
            tuteurs.add(tuteur);
        }

        return tuteurs;
    }

    public List<Integer> getAllTuteurIds() throws SQLException {
        List<Integer> tuteurIds = new ArrayList<>();
        String query = "SELECT idT FROM tuteurs";
        ResultSet rs = databaseconnection.getConnection().createStatement().executeQuery(query);

        while (rs.next()) {
            tuteurIds.add(rs.getInt("idT"));
        }
        return tuteurIds;
    }

    public boolean cinExiste(String cin) throws SQLException {
        String query = "SELECT COUNT(*) FROM tuteurs WHERE cinT = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean telephoneExiste(String telephone) throws SQLException {
        String query = "SELECT COUNT(*) FROM tuteurs WHERE telephoneT = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }



}

