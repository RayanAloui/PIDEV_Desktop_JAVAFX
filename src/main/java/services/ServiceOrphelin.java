package services;

import entities.Orphelin;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import entities.Tuteur;
import main.databaseconnection;

public class ServiceOrphelin implements IOrphelinService {

    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    private Connection connection;

    public ServiceOrphelin() {
        connection = databaseconnection.getConnection();
    }

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

    public boolean tuteurExiste(int idTuteur) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = databaseconnection.getConnection();
            String query = "SELECT COUNT(*) FROM tuteurs WHERE idT = ?";
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

    /*public List<String> getTuteurs() throws SQLException {
        List<String> tuteurs = new ArrayList<>();
        String query = "SELECT idT FROM tuteurs";

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tuteurs.add(String.valueOf(rs.getInt("idT")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des tuteurs : " + e.getMessage());
            throw e;
        }
        return tuteurs;
    }*/

    public List<String> getTuteurs() throws SQLException {
        List<String> tuteurs = new ArrayList<>();
        String query = "SELECT nomT, prenomT FROM tuteurs"; // Récupère Nom et Prénom

        try (Connection conn = databaseconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nomPrenom = rs.getString("nomT") + " " + rs.getString("prenomT"); // Concatenation Nom + Prénom
                tuteurs.add(nomPrenom);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des tuteurs : " + e.getMessage());
            throw e;
        }
        return tuteurs;
    }


    public List<Orphelin> getAllOrphelins() throws SQLException {
        List<Orphelin> listeOrphelins = new ArrayList<>();
        String query = "SELECT * FROM orphelins";

        try (Connection conn = databaseconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
                listeOrphelins.add(orphelin);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération des orphelins.", e);
        }
        return listeOrphelins;
    }

    public Map<Tuteur, Integer> getOrphelinsParTuteur() throws SQLException {
        Map<Tuteur, Integer> stats = new HashMap<>();

        String query = "SELECT idTuteur, COUNT(*) AS nombreOrphelins FROM orphelins GROUP BY idTuteur";
        try (Connection connection = databaseconnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int idTuteur = resultSet.getInt("idTuteur");
                int nombreOrphelins = resultSet.getInt("nombreOrphelins");

                Tuteur tuteur = serviceTuteur.getTuteurByID(idTuteur); // Récupérer le tuteur
                stats.put(tuteur, nombreOrphelins);
            }
        }

        return stats;
    }

    public List<Orphelin> getAllOrphelinsWithTuteur() throws SQLException {
        List<Orphelin> orphelins = new ArrayList<>();
        String query = "SELECT o.nomO, o.prenomO, o.dateNaissance, o.sexe, o.situationScolaire, t.nomT, t.prenomT " +
                "FROM orphelins o " +
                "JOIN tuteurs t ON o.idTuteur = t.idT";

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Tuteur tuteur = new Tuteur();
            tuteur.setNomT(rs.getString("nomT"));
            tuteur.setPrenomT(rs.getString("prenomT"));

            Orphelin orphelin = new Orphelin();
            orphelin.setNomO(rs.getString("nomO"));
            orphelin.setPrenomO(rs.getString("prenomO"));
            orphelin.setDateNaissance(rs.getString("dateNaissance"));
            orphelin.setSexe(rs.getString("sexe"));
            orphelin.setSituationScolaire(rs.getString("situationScolaire"));
            orphelin.setTuteur(tuteur);

            orphelins.add(orphelin);
        }
        return orphelins;
    }

    public Tuteur getTuteurById(int idTuteur) {
        try {
            Connection conn = databaseconnection.getConnection();
            String query = "SELECT * FROM tuteurs WHERE idT = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idTuteur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tuteur(rs.getInt("idT"), rs.getString("cinT"), rs.getString("nomT"),
                        rs.getString("prenomT"), rs.getString("telephoneT"), rs.getString("adresseT"),
                        rs.getString("disponibilite"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}


