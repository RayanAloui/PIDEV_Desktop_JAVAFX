package reclamations.services;

import reclamations.entities.Reponse;
import reclamations.main.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements Iservices<Reponse> {

    private Connection cnx;
    private static ReponseService instance;

    public ReponseService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public static ReponseService getInstance() {
        if (instance == null) {
            instance = new ReponseService();
        }
        return instance;
    }

    @Override
    public void ajouter(Reponse reponse) {
        String req = "INSERT INTO reponses (description, date, statut) VALUES (?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, reponse.getDescription());
            stm.setDate(2, reponse.getDate());
            stm.setString(3, reponse.getStatut());
            stm.executeUpdate();

            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reponse.setId(generatedKeys.getInt(1));
                    System.out.println("Réponse ajoutée : " + reponse);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Reponse reponse) {
        System.out.println("Modifier method called with reponse: " + reponse);

        if (cnx == null) {
            System.err.println("Database connection is null.");
            return;
        }

        try {
            cnx.setAutoCommit(false);

            if (reponse.getDescription() == null || reponse.getDescription().trim().isEmpty() ||
                    reponse.getStatut() == null ||
                    (!reponse.getStatut().equalsIgnoreCase("Traitee") &&
                            !reponse.getStatut().equalsIgnoreCase("Non Traitee"))) {
                System.out.println("Erreur : Données invalides pour la réponse.");
                return;
            }

            String query = "UPDATE reponses SET description = ?, date = ?, statut = ? WHERE id = ?";

            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, reponse.getDescription());
                pst.setDate(2, reponse.getDate());
                pst.setString(3, reponse.getStatut());
                pst.setInt(4, reponse.getId());

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    cnx.commit();
                    System.out.println("Réponse mise à jour avec succès !");
                } else {
                    System.out.println("Aucune mise à jour effectuée ! Vérifiez si l'ID existe.");
                }

            } catch (SQLException e) {
                System.err.println("Erreur SQL : " + e.getMessage());
                cnx.rollback();
            } finally {
                cnx.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Erreur base de données : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
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
    public List<Reponse> getall() {
        List<Reponse> reponsesList = new ArrayList<>();
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
