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
        // Log the reclamation object to check if it's passed correctly
        System.out.println("Modifier method called with reclamation: " + reclamation);

        if (cnx == null) {
            System.err.println("Database connection is null.");
            return;
        }

        try {
            // Disable autocommit before executing the update
            cnx.setAutoCommit(false);

            // Validate the data before proceeding
            if (reclamation.getMail() == null || reclamation.getMail().trim().isEmpty() ||
                    reclamation.getDescription() == null || reclamation.getDescription().trim().length() < 10 ||
                    reclamation.getStatut() == null ||
                    (!reclamation.getStatut().equalsIgnoreCase("Traitee") && !reclamation.getStatut().equalsIgnoreCase("Non Traitee"))) {
                System.out.println("Erreur : Données invalides pour la réclamation.");
                return;
            }


            // Log values to check what is being passed into the update
            System.out.println("Values being passed into update: " +
                    "Mail: " + reclamation.getMail() + ", " +
                    "Description: " + reclamation.getDescription() + ", " +
                    "Date: " + reclamation.getDate() + ", " +
                    "Statut: " + reclamation.getStatut() + ", " +
                    "ID: " + reclamation.getId());

            // SQL query for updating the reclamation
            String query = "UPDATE reclamations SET mail = ?, description = ?, date = ?, statut = ? WHERE id = ?";

            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                // Set the parameters for the SQL query
                pst.setString(1, reclamation.getMail());
                pst.setString(2, reclamation.getDescription());
                pst.setDate(3, reclamation.getDate());
                pst.setString(4, reclamation.getStatut());
                pst.setInt(5, reclamation.getId());

                // Log the values that are being passed into the SQL query
                System.out.println("SQL Query: " + query);
                System.out.println("Values: " +
                        reclamation.getMail() + ", " +
                        reclamation.getDescription() + ", " +
                        reclamation.getDate() + ", " +
                        reclamation.getStatut() + ", " +
                        reclamation.getId());

                // Execute the update query
                int rowsAffected = pst.executeUpdate();

                // Log how many rows were affected by the query
                System.out.println("Rows affected: " + rowsAffected);

                // Commit the transaction if rows are updated
                if (rowsAffected > 0) {
                    cnx.commit();  // Commit the transaction
                    System.out.println("Réclamation updated successfully!");
                } else {
                    System.out.println("No rows were updated. Please check the ID and values.");
                }

            } catch (SQLException e) {
                // Handle SQL exceptions
                System.err.println("Error during the update operation: " + e.getMessage());
                e.printStackTrace(); // Print full stack trace for debugging
                try {
                    // Rollback in case of error
                    cnx.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            } finally {
                // Restore autocommit to true after the transaction
                try {
                    cnx.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.err.println("Error while restoring autocommit: " + ex.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in setting autocommit: " + e.getMessage());
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