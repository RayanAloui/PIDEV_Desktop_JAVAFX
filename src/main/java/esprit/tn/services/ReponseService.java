package esprit.tn.services;
import esprit.tn.entities.Reponse;  // Avec majuscule
import esprit.tn.main.DatabaseConnection;
import esprit.tn.services.Iservices;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements Iservices<Reponse> {

    private Connection cnx;
    static ReponseService instance;

    // Constructeur public
    public ReponseService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Méthode pour obtenir l'instance unique de ReponseService
    public static ReponseService getInstance() {
        if (instance == null) {
            instance = new ReponseService();
        }
        return instance;
    }

    @Override
    public void ajouter(Reponse reponse) {
        // Validation des données
        if (reponse.getDescription() == null || reponse.getDescription().trim().length() < 10 ||
                reponse.getDate() == null ||
                reponse.getStatut() == null || (!reponse.getStatut().equals("Pending") &&
                !reponse.getStatut().equals("Resolved") && !reponse.getStatut().equals("Closed"))) {

            System.out.println("Erreur : Données invalides pour la réponse.");
            return;
        }

        // Requête SQL pour insérer une réponse
        String req = "INSERT INTO reponses (description, date, statut) VALUES (?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, reponse.getDescription());
            stm.setDate(2, reponse.getDate());
            stm.setString(3, reponse.getStatut());
            stm.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reponse.setId(generatedKeys.getInt(1));  // Met à jour l'id de la réponse avec l'ID généré
                    System.out.println("Réponse ajoutée : " + reponse);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Reponse reponse) {
        // Validation des données
        if (reponse.getDescription() == null || reponse.getDescription().trim().length() < 10 ||
                reponse.getDate() == null ||
                reponse.getStatut() == null || (!reponse.getStatut().equals("Pending") &&
                !reponse.getStatut().equals("Resolved") && !reponse.getStatut().equals("Closed"))) {

            System.out.println("Erreur : Données invalides pour la réponse.");
            return;
        }

        // Requête SQL pour mettre à jour une réponse
        String req = "UPDATE reponses SET description = ?, date = ?, statut = ? WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setString(1, reponse.getDescription());
            stm.setDate(2, reponse.getDate());
            stm.setString(3, reponse.getStatut());
            stm.setInt(4, reponse.getId());
            stm.executeUpdate();
            System.out.println("Réponse modifiée : " + reponse);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réponse : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        // Requête SQL pour supprimer une réponse
        String req = "DELETE FROM reponses WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            stm.executeUpdate();
            System.out.println("Réponse supprimée avec l'ID : " + id);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réponse : " + e.getMessage());
        }
    }

    @Override
    public List<Reponse> getall() {  // Method signature corrected to match interface
        List<Reponse> reponsesList = new ArrayList<>();
        // Requête SQL pour récupérer toutes les réponses
        String req = "SELECT * FROM reponses";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setDescription(rs.getString("description"));
                r.setDate(rs.getDate("date"));
                r.setStatut(rs.getString("statut"));
                reponsesList.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réponses : " + e.getMessage());
        }
        return reponsesList;
    }

    @Override
    public Reponse getone(int id) {
        Reponse r = null;
        // Requête SQL pour récupérer une réponse par son ID
        String req = "SELECT * FROM reponses WHERE id = ? LIMIT 1";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    r = new Reponse();
                    r.setId(rs.getInt("id"));
                    r.setDescription(rs.getString("description"));
                    r.setDate(rs.getDate("date"));
                    r.setStatut(rs.getString("statut"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la réponse : " + e.getMessage());
        }
        return r;
    }
}