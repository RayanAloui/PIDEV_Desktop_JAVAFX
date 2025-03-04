package esprit.tn.services;
import esprit.tn.entities.Cours;
import esprit.tn.entities.Rating;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import esprit.tn.main.databaseconnection1;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCours implements ICoursService {
    private Connection cnx = databaseconnection1.getConnection();

    @Override
    /*public void ajouterCours(Cours cours) {
        String req = "INSERT INTO cours (titre, contenu, resume, id_t,imageC) VALUES (?, ?, ?, ?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getContenu());
            String resume = EdenAIService.generateSummary(cours.getContenu());
            ps.setString(3, resume);
            ps.setInt(4, cours.getIdTuteur());
            ps.setString(5, cours.getImageC());
            ps.executeUpdate();
            System.out.println("Cours ajouté avec résumé IA !");
        } catch (SQLException e) {
            System.out.println("Erreur d'ajout : " + e.getMessage());
        }
    }*/

    /*public void ajouterCours(Cours cours) {
        String req = "INSERT INTO cours (titre, contenu, resume, id_t, imageC) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getContenu());

            // Simulation d'un appel à l'API EdenAI (remplace par l'appel réel)
            String jsonResponse = "{ \"openai\": { \"generated_text\": \"Résumé généré ici.\" } }";
            String resume;

            try {
                // Correction : Utilisation de getString pour extraire le texte généré
                resume = EdenAIService.generateSummary(jsonResponse);
                if (resume == null || resume.trim().isEmpty() || resume.startsWith("Erreur")) {
                    resume = "Résumé non disponible.";
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de la génération du résumé : " + e.getMessage());
                resume = "Résumé non disponible.";
            }

            ps.setString(3, resume);
            ps.setInt(4, cours.getIdTuteur());
            ps.setString(5, cours.getImageC());

            ps.executeUpdate();
            System.out.println("Cours ajouté avec résumé IA : " + resume);
        } catch (SQLException e) {
            System.out.println("Erreur d'ajout dans la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }*/


    public void ajouterCours(Cours cours) {
        String req = "INSERT INTO cours (titre, contenu, resume, id_t, imageC) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getContenu());

            // ✅ Génération du résumé proprement
            String resume = EdenAIService.generateSummary(cours.getContenu());
            ps.setString(3, resume);

            ps.setInt(4, cours.getIdTuteur());
            ps.setString(5, cours.getImageC());
            ps.executeUpdate();

            System.out.println("Cours ajouté avec résumé IA : " + resume);
        } catch (SQLException e) {
            System.out.println("Erreur d'ajout : " + e.getMessage());
        }
    }



    @Override
    public void modifierCours(Cours cours) {
        String req = "UPDATE cours SET titre=?, contenu=?, imageC=?, id_t=? WHERE idC=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getContenu());
            ps.setString(3, cours.getImageC());
            ps.setInt(4, cours.getIdTuteur());
            ps.setInt(5, cours.getIdC());
            ps.executeUpdate();
            System.out.println("Cours modifié !");
        } catch (SQLException e) {
            System.out.println("Erreur de modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimerCours(int idC) {
        String req = "DELETE FROM cours WHERE idC=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idC);
            ps.executeUpdate();
            System.out.println("Cours supprimé !");
        } catch (SQLException e) {
            System.out.println("Erreur de suppression : " + e.getMessage());
        }
    }

    public void supprimerCoursD(int idCours) {
        String deleteRatingsQuery = "DELETE FROM ratings WHERE id_cours = ?";
        String deleteCoursQuery = "DELETE FROM cours WHERE idC = ?";

        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement psRatings = conn.prepareStatement(deleteRatingsQuery);
             PreparedStatement psCours = conn.prepareStatement(deleteCoursQuery)) {

            // Supprimer les évaluations liées au cours
            psRatings.setInt(1, idCours);
            psRatings.executeUpdate();

            // Supprimer le cours après avoir supprimé ses dépendances
            psCours.setInt(1, idCours);
            psCours.executeUpdate();

            System.out.println("Cours et évaluations supprimés avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Cours> afficherCours() {
        List<Cours> list = new ArrayList<>();
        String req = "SELECT * FROM cours";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                list.add(new Cours(
                        rs.getInt("idC"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("resume"),
                        rs.getString("imageC"),
                        rs.getInt("id_t")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur d'affichage : " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Cours> afficherCoursParTuteur(int idTuteur) {
        List<Cours> list = new ArrayList<>();
        String req = "SELECT * FROM cours WHERE id_t=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idTuteur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Cours(
                        rs.getInt("idC"),
                        rs.getString("titre"),
                        rs.getString("contenu"),
                        rs.getString("imageC"),
                        rs.getInt("id_t")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur d'affichage : " + e.getMessage());
        }
        return list;
    }


    public List<Cours> getAllCours() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT idC, titre, contenu, imageC FROM cours";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idC = resultSet.getInt("idC");
                String titre = resultSet.getString("titre");
                String contenu = resultSet.getString("contenu");
                String imageC = resultSet.getString("imageC");

                coursList.add(new Cours(idC, titre, contenu, imageC));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return coursList;
    }

    public List<Cours> getCoursByTuteur(int idTuteur) {
        List<Cours> coursList = new ArrayList<>();

        String query = "SELECT * FROM cours WHERE id_t = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, idTuteur);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cours cours = new Cours();
                cours.setIdC(rs.getInt("idC"));
                cours.setTitre(rs.getString("titre"));
                cours.setContenu(rs.getString("contenu"));
                cours.setResume(rs.getString("resume"));
                cours.setImageC(rs.getString("imageC"));
                coursList.add(cours);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coursList;
    }

    public void ajouterRating(int idOrphelin, int idCours, int note) {
        try (Connection con = databaseconnection1.getConnection()) {
            // Vérifier si l'orphelin a déjà noté ce cours
            String checkQuery = "SELECT * FROM ratings WHERE id_orphelin = ? AND id_cours = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, idOrphelin);
            checkStmt.setInt(2, idCours);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Mise à jour de la note
                String updateQuery = "UPDATE ratings SET note = ? WHERE id_orphelin = ? AND id_cours = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, note);
                updateStmt.setInt(2, idOrphelin);
                updateStmt.setInt(3, idCours);
                updateStmt.executeUpdate();
            } else {
                // Ajout d'une nouvelle note
                String insertQuery = "INSERT INTO ratings (id_orphelin, id_cours, note) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, idOrphelin);
                insertStmt.setInt(2, idCours);
                insertStmt.setInt(3, note);
                insertStmt.executeUpdate();
            }

            // Mettre à jour la note moyenne
            updateNoteMoyenne(idCours);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateNoteMoyenne(int idCours) {
        try (Connection con = databaseconnection1.getConnection()) {
            String avgQuery = "SELECT AVG(note) AS moyenne FROM ratings WHERE id_cours = ?";
            PreparedStatement avgStmt = con.prepareStatement(avgQuery);
            avgStmt.setInt(1, idCours);
            ResultSet rs = avgStmt.executeQuery();

            if (rs.next()) {
                double moyenne = rs.getDouble("moyenne");

                String updateQuery = "UPDATE cours SET note_moyenne = ? WHERE idC = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setDouble(1, moyenne);
                updateStmt.setInt(2, idCours);
                updateStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getNoteMoyenne(int idCours) {
        String query = "SELECT AVG(note) FROM ratings WHERE id_cours = ?";
        double moyenne = 0.0;

        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCours);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                moyenne = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moyenne;
    }


    public ObservableList<Rating> getRatings(int idCours) {
        ObservableList<Rating> ratings = FXCollections.observableArrayList();
        String query = "SELECT o.nomO, o.prenomO, r.note FROM ratings r " +
                "JOIN orphelins o ON r.id_orphelin = o.idO " +
                "WHERE r.id_cours = ?";

        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCours);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nomO");
                String prenom = rs.getString("prenomO");
                int note = rs.getInt("note");
                ratings.add(new Rating(nom, prenom, note));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ratings;
    }

    public boolean checkIfAlreadyRated(int idOrphelin, int idCours) {
        String query = "SELECT COUNT(*) FROM ratings WHERE id_orphelin = ? AND id_cours = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idOrphelin);
            pst.setInt(2, idCours);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertRating(int idOrphelin, int idCours, int rating) {
        String query = "INSERT INTO ratings (id_orphelin, id_cours, note) VALUES (?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idOrphelin);
            pst.setInt(2, idCours);
            pst.setInt(3, rating);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRating(int idOrphelin, int idCours, int rating) {
        String query = "UPDATE ratings SET note = ? WHERE id_orphelin = ? AND id_cours = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, rating);
            pst.setInt(2, idOrphelin);
            pst.setInt(3, idCours);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cours> getCoursByTuteurs(int idTuteur) {
        List<Cours> coursList = new ArrayList<>();
        String req = "SELECT * FROM cours WHERE id_t = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, idTuteur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cours c = new Cours(rs.getInt("idC"), rs.getString("titre"),
                        rs.getString("contenu"), rs.getString("imageC"), rs.getInt("id_t"));
                coursList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }


}

