package esprit.tn.services;

import esprit.tn.entities.visites;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitesService implements Iservices<visites> {

    Connection cnx;
    static VisitesService instance;

    // Constructeur public
    public VisitesService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    // Méthode pour obtenir l'instance unique de VisitesService
    public static VisitesService getInstance() {
        if (instance == null) {
            instance = new VisitesService();
        }
        return instance;
    }

    @Override
    public void ajouter(visites visite) throws SQLException {

            String checkVisitorQuery = "SELECT COUNT(*) FROM visiteurs WHERE id = ?";

            try (PreparedStatement stmCheckVisitor = cnx.prepareStatement(checkVisitorQuery)) {
                stmCheckVisitor.setInt(1, visite.getId_visiteur());
                try (ResultSet rs = stmCheckVisitor.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) { // Si le visiteur existe
                        String req = "INSERT INTO visites (id_visiteur, date, heure, motif, statut) VALUES (?, ?, ?, ?, ?)";

                        try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                            stm.setInt(1, visite.getId_visiteur());
                            stm.setDate(2, java.sql.Date.valueOf(visite.getDate()));
                            stm.setTime(3, java.sql.Time.valueOf(visite.getHeure()));
                            stm.setString(4, visite.getMotif());
                            stm.setString(5, visite.getStatut());
                            stm.executeUpdate();

                            // Récupérer l'ID généré
                            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    visite.setId(generatedKeys.getInt(1));  // Met à jour l'id de la visite avec l'ID généré
                                    System.out.println("Visite ajoutée : " + visite);
                                }
                            }
                        } catch (SQLException e) {
                            System.err.println("Erreur lors de l'ajout de la visite : " + e.getMessage());
                        }
                    } else {
                        System.out.println("Erreur : Le visiteur avec l'ID " + visite.getId_visiteur() + " n'existe pas.");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la vérification du visiteur : " + e.getMessage());
            }
    }

    @Override
    public void modifier(visites visite) {
        try {
            if (visite.getDate() == null ||
                    visite.getHeure() == null ||
                    visite.getMotif() == null || visite.getMotif().trim().length() < 3 ||
                    visite.getStatut() == null || (!visite.getStatut().equals("Accepté") &&
                    !visite.getStatut().equals("Refusé") && !visite.getStatut().equals("En attente")) ||
                    visite.getId_visiteur() <= 0) {
                throw new IllegalArgumentException("Erreur : Données invalides pour la visite.");
            }

            String req = "UPDATE visites SET id_visiteur = ?, date = ?, heure = ?, motif = ?, statut = ? WHERE id = ?";

            try (PreparedStatement stm = cnx.prepareStatement(req)) {
                stm.setInt(1, visite.getId_visiteur());
                stm.setDate(2, java.sql.Date.valueOf(visite.getDate()));
                stm.setTime(3, java.sql.Time.valueOf(visite.getHeure()));
                stm.setString(4, visite.getMotif());
                stm.setString(5, visite.getStatut());
                stm.setInt(6, visite.getId());
                stm.executeUpdate();
                System.out.println("Visite modifiée : " + visite);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la mise à jour de la visite : " + e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM visites WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la visite : " + e.getMessage());
        }
    }

    @Override
    public List<visites> getall() {
        List<visites> visitesList = new ArrayList<>();
        String req = "SELECT v.id, v.id_visiteur, v.date, v.heure, v.motif, v.statut, " +
                "vi.nom, vi.prenom FROM visites v " +
                "INNER JOIN visiteurs vi ON v.id_visiteur = vi.id";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                visites v = new visites();
                v.setId(rs.getInt("id"));
                v.setId_visiteur(rs.getInt("id_visiteur"));
                v.setDate(rs.getDate("date").toLocalDate());
                v.setHeure(rs.getTime("heure").toLocalTime());
                v.setMotif(rs.getString("motif"));
                v.setStatut(rs.getString("statut"));

                // Ajouter les informations du visiteur si nécessaire
                String visiteurNom = rs.getString("nom");
                String visiteurPrenom = rs.getString("prenom");

                // Par exemple, vous pourriez aussi avoir une classe `Visiteur` pour représenter le visiteur
                // v.setVisiteur(new Visiteur(visiteurNom, visiteurPrenom));

                visitesList.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des visites : " + e.getMessage());
        }
        return visitesList;
    }

    @Override
    public visites getone(int id_visiteur) {
        visites v = null;
        String req = "SELECT * FROM visites WHERE id_visiteur = ? LIMIT 1"; // Récupère la première visite correspondant à l'id_visiteur

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setInt(1, id_visiteur); // Paramètre : id_visiteur
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    v = new visites();
                    v.setId(rs.getInt("id"));
                    v.setId_visiteur(rs.getInt("id_visiteur"));
                    v.setDate(rs.getDate("date").toLocalDate());
                    v.setHeure(rs.getTime("heure").toLocalTime());
                    v.setMotif(rs.getString("motif"));
                    v.setStatut(rs.getString("statut"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la visite par visiteur : " + e.getMessage());
        }
        return v;
    }
}
