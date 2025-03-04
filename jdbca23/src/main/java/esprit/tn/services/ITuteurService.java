package esprit.tn.services;

import esprit.tn.entities.Reclamation;
import esprit.tn.entities.Reponse;
import esprit.tn.entities.Tuteur;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface ITuteurService {
    void ajouter(Tuteur t) throws SQLException;
    void updateTuteur(Tuteur t) throws SQLException;
    void delete(int id) throws SQLException;
    ArrayList<Tuteur> afficherAll() throws SQLException;

    interface Iservices3<T> {
        void ajouter(T t); // Add an entity
        void modifier(T t); // Update an entity
        void supprimer(int id); // Delete an entity by ID
        List<T> getall(); // Get all entities
        T getone(int id); // Get one entity by ID
    }

    class ReclamationService implements Iservices3<Reclamation> {

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
            String req = "INSERT INTO reclamations (mail, description, date, typereclamation) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, reclamation.getMail());
                stm.setString(2, reclamation.getDescription());
                stm.setDate(3, reclamation.getDate());
                stm.setString(4, reclamation.getTypereclamation());
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
                        reclamation.getDescription() == null || reclamation.getDescription().trim().isEmpty() ||
                        reclamation.getTypereclamation() == null ||
                        !(reclamation.getTypereclamation().equalsIgnoreCase("Bien-être et droits des enfants") ||
                                reclamation.getTypereclamation().equalsIgnoreCase("Gestion et administration") ||
                                reclamation.getTypereclamation().equalsIgnoreCase("Conditions de vie"))) {
                    System.out.println("Erreur : Données invalides pour la réclamation.");
                    return;
                }



                // Debug: Log description length
                System.out.println("Description before update: " + reclamation.getDescription());
                System.out.println("Description length: " + reclamation.getDescription().length());

                // SQL query for updating the reclamation
                String query = "UPDATE reclamations SET mail = ?, description = ?, date = ?, typereclamation = ? WHERE id = ?";

                try (PreparedStatement pst = cnx.prepareStatement(query)) {
                    pst.setString(1, reclamation.getMail());
                    pst.setString(2, reclamation.getDescription().trim());  // Trim spaces
                    pst.setDate(3, reclamation.getDate());
                    pst.setString(4, reclamation.getTypereclamation());
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
                    r.setTypereclamation(rs.getString("typereclamation"));
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
                        r.setTypereclamation(rs.getString("typereclamation"));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération de la réclamation : " + e.getMessage());
            }
            return r;
        }
    }

    class ReponseService implements Iservices3<Reponse> {

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
            String req = "INSERT INTO reponses (description, date, statut,indice) VALUES (?, ?, ?,?)";

            try (PreparedStatement stm = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, reponse.getDescription());
                stm.setDate(2, reponse.getDate());
                stm.setString(3, reponse.getStatut());
                stm.setInt(4, reponse.getIndice());
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
                    r.setIndice(rs.getInt("indice"));
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
                        r.setIndice(rs.getInt("indice"));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération de la réponse : " + e.getMessage());
            }
            return r;
        }
    }
}
