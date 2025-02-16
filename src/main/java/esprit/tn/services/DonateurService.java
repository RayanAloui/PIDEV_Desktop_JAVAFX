package esprit.tn.services;


import esprit.tn.entities.donateur;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonateurService implements Iservice1<donateur> {

    private Connection cnx;
    private static DonateurService instance;

    public DonateurService(){

        cnx= DatabaseConnection.getInstance().getCnx();
    }
    public static DonateurService getInstance() {
        if (instance == null) {
            // Si l'instance n'existe pas, on la crée
            instance = new DonateurService();
        }
        return instance;
    }

    @Override
    public void ajouter(donateur donateur) {
        if (donateur.getNom() == null || donateur.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }

        if (donateur.getPrenom() == null || donateur.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }

        if (donateur.getAdresse() == null || donateur.getAdresse().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
        }

        if (donateur.getEmail() == null || !donateur.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("L'email est invalide.");
        }

        if (String.valueOf(donateur.getTelephone()).length() != 8) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 8 chiffres.");
        }

        String sql="INSERT INTO donateur (nom,prenom,adresse,email,telephone) VALUES (?,?,?,?,?)";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, donateur.getNom());
            stm.setString(2, donateur.getPrenom());
            stm.setString(3, donateur.getAdresse());
            stm.setString(4, donateur.getEmail());
            stm.setInt(5, donateur.getTelephone());

            stm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void modifier(donateur donateur) {
        if (donateur.getNom() == null || donateur.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }

        if (donateur.getPrenom() == null || donateur.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }

        if (donateur.getAdresse() == null || donateur.getAdresse().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
        }

        if (donateur.getEmail() == null || !donateur.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("L'email est invalide.");
        }

        if (String.valueOf(donateur.getTelephone()).length() != 8) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 8 chiffres.");
        }
        String sql = "UPDATE donateur SET nom = ?, prenom = ?, adresse = ?, email = ?, telephone = ? WHERE id = ? ";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, donateur.getNom());
            stm.setString(2, donateur.getPrenom());
            stm.setString(3, donateur.getAdresse());
            stm.setString(4, donateur.getEmail());
            stm.setInt(5, donateur.getTelephone());
            stm.setInt(6, donateur.getId());

            stm.executeUpdate();
            System.out.println("Donateur modifié avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM donateur WHERE id = ?";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, id);

            int rows = stm.executeUpdate();
            if (rows > 0) {
                System.out.println("Donateur supprimé avec succès !");
            } else {
                System.out.println("Aucun donateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<donateur> getall() {
        List<donateur> dt=new ArrayList<>();

        String req="SELECT * FROM donateur";

        try {
            Statement stm=cnx.createStatement();
            ResultSet rs= stm.executeQuery(req);

            while (rs.next()){
                donateur d=new donateur();
                d.setId( rs.getInt("Id"));
                d.setNom(  rs.getString("Nom"));
                d.setPrenom( rs.getString("Prenom"));
                d.setEmail(rs.getString("Email"));
                d.setTelephone(rs.getInt("Telephone"));
                d.setAdresse(rs.getString("Adresse"));

                dt.add(d);
            }

            System.out.println(dt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dt;
    }

    @Override
    public donateur getone(int id) {
        String sql = "SELECT * FROM donateur WHERE id = ?";
        donateur dt = null;

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                dt = new donateur();
                dt.setId(rs.getInt("id"));
                dt.setNom(rs.getString("nom"));
                dt.setPrenom(rs.getString("prenom"));
                dt.setAdresse(rs.getString("adresse"));
                dt.setEmail(rs.getString("email"));
                dt.setTelephone(rs.getInt("telephone"));
            }
            System.out.println(dt);

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return dt;
    }



    public List<Integer> getAllIds() {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT id FROM donateur";

        // Do not close the connection here.
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getInstance().getCnx(); // Get connection from the singleton
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources manually if necessary, but not the connection if it's shared
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Don't close the connection if you want to keep it open for other uses
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ids;
    }

}
