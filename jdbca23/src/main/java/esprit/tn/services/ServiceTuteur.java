package esprit.tn.services;

import esprit.tn.entities.Tuteur;

import java.sql.*;
import java.util.*;

import esprit.tn.main.databaseconnection1;



public class ServiceTuteur implements ITuteurService {

    private Connection connection;

    public ServiceTuteur() {
        connection = databaseconnection1.getConnection();
    }

    @Override
    public void ajouter(Tuteur t) throws SQLException {
        // Vérification des entrées utilisateur
        if (!t.getCinT().trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("CIN invalide : doit contenir exactement 8 chiffres.");
        }
        if (!t.getNomT().trim().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Nom invalide : doit contenir uniquement des lettres et des espaces.");
        }
        if (!t.getPrenomT().trim().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Prénom invalide : doit contenir uniquement des lettres et des espaces.");
        }
        if (!t.getTelephoneT().trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("Téléphone invalide : doit contenir exactement 8 chiffres.");
        }
        if (t.getAdresseT().trim().isEmpty()) {
            throw new IllegalArgumentException("Adresse invalide : elle ne doit pas être vide.");
        }
        if (!t.getEmail().trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email invalide.");
        }
        if (!t.getDisponibilite().trim().equalsIgnoreCase("oui") && !t.getDisponibilite().trim().equalsIgnoreCase("non")) {
            throw new IllegalArgumentException("Disponibilité invalide : doit être 'oui' ou 'non'.");
        }

        // Vérification des doublons (pour un nouvel ajout, l'ID est mis à 0)
        if (cinExiste(t.getCinT(), 0)) {
            throw new SQLException("Ce numéro de CIN est déjà utilisé !");
        }
        if (telephoneExiste(t.getTelephoneT(), 0)) {
            throw new SQLException("Ce numéro de téléphone est déjà utilisé !");
        }
        if (emailExiste(t.getEmail(), 0)) {
            throw new SQLException("Cet email est déjà utilisé !");
        }

        // Connexion à la base de données et insertion
        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                     "INSERT INTO tuteurs (cinT, nomT, prenomT, telephoneT, adresseT, disponibilite, email) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            pst.setString(1, t.getCinT().trim());
            pst.setString(2, t.getNomT().trim());
            pst.setString(3, t.getPrenomT().trim());
            pst.setString(4, t.getTelephoneT().trim());
            pst.setString(5, t.getAdresseT().trim());
            pst.setString(6, t.getDisponibilite().trim());
            pst.setString(7, t.getEmail().trim());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tuteur ajouté avec succès.");
            } else {
                System.out.println("L'ajout du tuteur a échoué.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout du tuteur.");
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
        if (!t.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Email invalide.");
        }
        if (!t.getDisponibilite().equalsIgnoreCase("oui") && !t.getDisponibilite().equalsIgnoreCase("non")) {
            throw new IllegalArgumentException("Disponibilité invalide : doit être 'oui' ou 'non'.");
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String ancienneDisponibilite = getDisponibiliteTuteur(t.getIdT());

        try {
            conn = databaseconnection1.getConnection();

            // Récupérer l'ancienne valeur de la disponibilité
            String getDisponibiliteQuery = "SELECT disponibilite FROM tuteurs WHERE idT = ?";
            try (PreparedStatement getDispStmt = conn.prepareStatement(getDisponibiliteQuery)) {
                getDispStmt.setInt(1, t.getIdT());
                rs = getDispStmt.executeQuery();
                if (rs.next()) {
                    ancienneDisponibilite = rs.getString("disponibilite");
                } else {
                    throw new SQLException("Tuteur introuvable avec l'ID : " + t.getIdT());
                }
            }

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

            // Vérifier si l'email est déjà utilisé par un autre tuteur
            String checkEmailQuery = "SELECT idT FROM tuteurs WHERE email = ? AND idT != ?";
            try (PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery)) {
                checkEmailStmt.setString(1, t.getEmail());
                checkEmailStmt.setInt(2, t.getIdT());
                rs = checkEmailStmt.executeQuery();
                if (rs.next()) {
                    throw new SQLException("L'email " + t.getEmail() + " est déjà utilisé par un autre tuteur !");
                }
            }

            // Mise à jour du tuteur
            String updateQuery = "UPDATE tuteurs SET cinT = ?, nomT = ?, prenomT = ?, telephoneT = ?, adresseT = ?, disponibilite = ?, email = ? WHERE idT = ?";
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, t.getCinT());
            pst.setString(2, t.getNomT());
            pst.setString(3, t.getPrenomT());
            pst.setString(4, t.getTelephoneT());
            pst.setString(5, t.getAdresseT());
            pst.setString(6, t.getDisponibilite());
            pst.setString(7, t.getEmail());
            pst.setInt(8, t.getIdT());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Tuteur mis à jour avec succès.");

                // Vérifier si la disponibilité a changé
                if (!t.getDisponibilite().equalsIgnoreCase(ancienneDisponibilite)) {
                    notifierTuteurs(t.getIdT(), t.getDisponibilite());
                    String message = "Mise à jour : Le tuteur " + t.getNomT() + " " + t.getPrenomT() +
                            " est maintenant " + t.getDisponibilite().toUpperCase() + " disponible ";

                    // Récupérer les numéros des autres tuteurs et envoyer un SMS
                    List<String> numerosTuteurs = recupererNumerosTuteurs(t.getIdT());
                    SmsService.envoyerNotificationSMS(numerosTuteurs, message);
                }
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

    /**
     * Notifie tous les autres tuteurs du changement de disponibilité.
     */
    private void notifierTuteurs(int idTuteurModifie, String nouvelleDisponibilite) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = databaseconnection1.getConnection();
            String selectEmailsQuery = "SELECT email FROM tuteurs WHERE idT != ?";
            pst = conn.prepareStatement(selectEmailsQuery);
            pst.setInt(1, idTuteurModifie);
            rs = pst.executeQuery();

            EmailService emailService = new EmailService();
            String sujet = "Mise à jour de disponibilité d'un tuteur";
            String contenu = "<p>Un tuteur a mis à jour sa disponibilité.</p>" +
                    "<p>Nouvelle disponibilité : <b>" + nouvelleDisponibilite + "</b></p>" +
                    "<p>Connectez-vous pour voir les détails.</p>";

            while (rs.next()) {
                String email = rs.getString("email");
                emailService.envoyerEmail(email, sujet, contenu);
            }

            System.out.println("Notification envoyée aux autres tuteurs.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'envoi des notifications : " + e.getMessage());
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
            conn = databaseconnection1.getConnection();
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
            conn = databaseconnection1.getConnection();
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
                String disponibilite = rs.getString("disponibilite");
                String email = rs.getString("email");

                Tuteur tuteur = new Tuteur(cin, nom, prenom, telephone, adresse,disponibilite,email);
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

    public List<Tuteur> afficherTuteurs() {
        List<Tuteur> list = new ArrayList<>();
        String req = "SELECT * FROM tuteurs";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                list.add(new Tuteur(
                        rs.getInt("idT"),
                        rs.getString("cinT"),
                        rs.getString("nomT"),
                        rs.getString("prenomT"),
                        rs.getString("telephoneT"),
                        rs.getString("adresseT"),
                        rs.getString("disponibilite"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur affichage tuteurs : " + e.getMessage());
        }
        return list;
    }


    private boolean tuteurExiste(int idTuteur) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = databaseconnection1.getConnection();
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
            conn = databaseconnection1.getConnection();
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
                String disponibilite = rs.getString("disponibilite");
                String email = rs.getString("email");

                // Créer un objet Tuteur avec les données récupérées
                tuteur = new Tuteur(id, cin, nom, prenom, telephone, adresse,disponibilite,email);
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


    public Tuteur getTuteurBYID(int idTuteur) {
        try {
            Connection conn = databaseconnection1.getConnection();
            String query = "SELECT * FROM tuteurs WHERE idT = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idTuteur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Tuteur(
                        rs.getInt("idT"),
                        rs.getString("nomT"),
                        rs.getString("prenomT")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Tuteur> getAllTuteurss() throws SQLException {
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
                    rs.getString("adresseT"),
                    rs.getString("disponibilite"),
                    rs.getString("email")
            );
            tuteurs.add(tuteur);
        }

        return tuteurs;
    }

    public Map<Integer, String> getAllTuteurs() throws SQLException {
        Map<Integer, String> tuteurs = new HashMap<>();
        String query = "SELECT idT, nomT, prenomT FROM tuteurs"; // Récupération ID, Nom et Prénom

        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("idT");
                String nomPrenom = rs.getString("nomT") + " " + rs.getString("prenomT");
                tuteurs.put(id, nomPrenom);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des tuteurs : " + e.getMessage());
            throw e;
        }
        return tuteurs;
    }

    public List<Tuteur> getAllTuteursss() throws SQLException {
        List<Tuteur> tuteurs = new ArrayList<>();
        String query = "SELECT * FROM tuteurs";

        try (Connection conn = databaseconnection1.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Tuteur tuteur = new Tuteur(
                        rs.getInt("idT"),
                        rs.getString("nomT"),
                        rs.getString("prenomT")
                );
                tuteurs.add(tuteur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des tuteurs : " + e.getMessage());
            throw e;
        }

        return tuteurs;
    }



    public List<Integer> getAllTuteurIds() throws SQLException {
        List<Integer> tuteurIds = new ArrayList<>();
        String query = "SELECT idT FROM tuteurs";
        ResultSet rs = databaseconnection1.getConnection().createStatement().executeQuery(query);

        while (rs.next()) {
            tuteurIds.add(rs.getInt("idT"));
        }
        return tuteurIds;
    }

    public boolean cinExiste(String cin, int idT) throws SQLException {
        String query = "SELECT COUNT(*) FROM tuteurs WHERE cinT = ? AND idT != ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cin);
            stmt.setInt(2, idT); // Exclure le même tuteur
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean telephoneExiste(String telephone, int idT) throws SQLException {
        String query = "SELECT COUNT(*) FROM tuteurs WHERE telephoneT = ? AND idT != ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telephone);
            stmt.setInt(2, idT); // Exclure le même tuteur
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean emailExiste(String email, int idT) throws SQLException {
        String query = "SELECT COUNT(*) FROM tuteurs WHERE email = ? AND idT != ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setInt(2, idT); // Exclure le même tuteur
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    public Tuteur getTuteurByID(int idTuteur) throws SQLException {
        String query = "SELECT * FROM tuteurs WHERE idT = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, idTuteur);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Tuteur(
                    rs.getInt("idT"),
                    rs.getString("nomT"),
                    rs.getString("prenomT")
            );
        }
        return new Tuteur(0, "Inconnu", "");
    }


    // Récupérer la disponibilité actuelle d’un tuteur avant modification
    private String getDisponibiliteTuteur(int idTuteur) throws SQLException {
        String disponibilite = "";
        String query = "SELECT disponibilite FROM tuteurs WHERE idT = ?";

        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idTuteur);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    disponibilite = rs.getString("disponibilite");
                }
            }
        }
        return disponibilite;
    }

    // Récupérer les numéros de téléphone des autres tuteurs
    private List<String> recupererNumerosTuteurs(int idExclu) throws SQLException {
        List<String> numeros = new ArrayList<>();
        String query = "SELECT telephoneT FROM tuteurs WHERE idT != ?";

        try (Connection conn = databaseconnection1.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idExclu);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    numeros.add("+216" + rs.getString("telephoneT"));
                }
            }
        }
        return numeros;
    }

    // Récupérer le nom et prénom d'un tuteur par son ID
    public String getNomPrenomTuteur(int idTuteur) throws SQLException {
        String query = "SELECT nomT, prenomT FROM tuteurs WHERE idT = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idTuteur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nomT") + " " + rs.getString("prenomT");
            }
        }
        return "Inconnu";
    }

    // Récupérer l'ID d'un tuteur à partir de son nom complet
    public int getIdTuteurByName(String nomTuteur) throws SQLException {
        String query = "SELECT idT FROM tuteurs WHERE CONCAT(nomT, ' ', prenomT) = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nomTuteur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idT");
            }
        }
        return -1; // Retourne -1 si le tuteur n'est pas trouvé
    }

    // Récupérer la liste des noms et prénoms des tuteurs
    public List<String> getTuteursNoms() throws SQLException {
        List<String> tuteurs = new ArrayList<>();
        String query = "SELECT CONCAT(nomT, ' ', prenomT) AS nomComplet FROM tuteurs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tuteurs.add(rs.getString("nomComplet"));
            }
        }
        return tuteurs;
    }

}

