package reclamations.services;

import reclamations.entities.Reclamation;
import reclamations.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements Iservices<Reclamation> {

    private Connection cnx;
    private static ReclamationService instance;

    // Constructeur public
    public ReclamationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Méthode pour obtenir l'instance unique de ReclamationService
    public static ReclamationService getInstance() {
        if (instance == null) {
            instance = new ReclamationService();
        }
        return instance;
    }

    @Override
    public void ajouter(Reclamation reclamation) {
        // Validation des données


        // Requête SQL pour insérer une réclamation
        String req = "INSERT INTO reclamations (mail, description, date, statut) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, reclamation.getMail());
            stm.setString(2, reclamation.getDescription());
            stm.setDate(3, reclamation.getDate());
            stm.setString(4, reclamation.getStatut());
            stm.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reclamation.setId(generatedKeys.getInt(1));
                    System.out.println("Réclamation ajoutée : " + reclamation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }
    }

    @Override

    public void modifier(Reclamation reclamation) {
        System.out.println("Modifier method called with reclamation: " + reclamation);

        if (cnx == null) {
            System.err.println("Database connection is null.");
            return;
        }

        try {
            cnx.setAutoCommit(false);

            // Validate input
            if (reclamation.getMail() == null || reclamation.getMail().trim().isEmpty() ||
                    reclamation.getDescription() == null || reclamation.getDescription().trim().length() < 1 ||
                    reclamation.getStatut() == null ||
                    (!reclamation.getStatut().equalsIgnoreCase("Traitee") &&
                            !reclamation.getStatut().equalsIgnoreCase("Non Traitee"))) {
                System.out.println("Erreur : Données invalides pour la réclamation.");
                return;
            }

            // Debug: Log description length
            System.out.println("Description before update: " + reclamation.getDescription());
            System.out.println("Description length: " + reclamation.getDescription().length());

            // SQL query for updating the reclamation
            String query = "UPDATE reclamations SET mail = ?, description = ?, date = ?, statut = ? WHERE id = ?";

            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, reclamation.getMail());
                pst.setString(2, reclamation.getDescription().trim());  // Trim spaces
                pst.setDate(3, reclamation.getDate());
                pst.setString(4, reclamation.getStatut());
                pst.setInt(5, reclamation.getId());

                int rowsAffected = pst.executeUpdate();

                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected > 0) {
                    cnx.commit();
                    System.out.println("Réclamation updated successfully!");
                } else {
                    System.out.println("No rows updated! Check if the ID exists.");
                }

            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                cnx.rollback();
            } finally {
                cnx.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }



    @Override
    public void supprimer(int id) {
        // Requête SQL pour supprimer une réclamation
        String req = "DELETE FROM reclamations WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            stm.executeUpdate();
            System.out.println("Réclamation supprimée avec l'ID : " + id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
        }
    }

    @Override
    public List<Reclamation> getall() {
        List<Reclamation> reclamationsList = new ArrayList<>();
        // Requête SQL pour récupérer toutes les réclamations
        String req = "SELECT * FROM reclamations";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));
                r.setMail(rs.getString("mail"));
                r.setDescription(rs.getString("description"));
                r.setDate(rs.getDate("date"));
                r.setStatut(rs.getString("statut"));
                reclamationsList.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
        }
        return reclamationsList;
    }

    @Override
    public Reclamation getone(int id) {
        Reclamation r = null;
        // Requête SQL pour récupérer une réclamation par son ID
        String req = "SELECT * FROM reclamations WHERE id = ? LIMIT 1";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    r = new Reclamation();
                    r.setId(rs.getInt("id"));
                    r.setMail(rs.getString("mail"));
                    r.setDescription(rs.getString("description"));
                    r.setDate(rs.getDate("date"));
                    r.setStatut(rs.getString("statut"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la réclamation : " + e.getMessage());
        }
        return r;
    }
}