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
        if (reclamation.getMail() == null || reclamation.getMail().trim().isEmpty() ||
                reclamation.getDescription() == null || reclamation.getDescription().trim().length() < 10 ||
                reclamation.getDate() == null ||
                reclamation.getStatut() == null || (!reclamation.getStatut().equals("Pending") &&
                !reclamation.getStatut().equals("Resolved") && !reclamation.getStatut().equals("Closed"))) {

            System.out.println("Erreur : Données invalides pour la réclamation.");
            return;
        }

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
        // Validation des données
        if (reclamation.getMail() == null || reclamation.getMail().trim().isEmpty() ||
                reclamation.getDescription() == null || reclamation.getDescription().trim().length() < 10 ||
                reclamation.getDate() == null ||
                reclamation.getStatut() == null || (!reclamation.getStatut().equals("Pending") &&
                !reclamation.getStatut().equals("Resolved") && !reclamation.getStatut().equals("Closed"))) {

            System.out.println("Erreur : Données invalides pour la réclamation.");
            return;
        }

        // Requête SQL pour mettre à jour une réclamation
        String req = "UPDATE reclamations SET mail = ?, description = ?, date = ?, statut = ? WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, reclamation.getMail());
            stm.setString(2, reclamation.getDescription());
            stm.setDate(3, reclamation.getDate());
            stm.setString(4, reclamation.getStatut());
            stm.setInt(5, reclamation.getId());
            stm.executeUpdate();
            System.out.println("Réclamation modifiée : " + reclamation);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
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