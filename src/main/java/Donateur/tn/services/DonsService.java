package Donateur.tn.services;

import java.util.List;
import Donateur.tn.entities.Dons;
import Donateur.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;


public class DonsService implements Iservice<Dons> {

    private Connection cnx;

    public DonsService() {

        cnx = DatabaseConnection.instance.getCnx();
    }

    @Override
    public void ajouter(Dons dons) {

        if (dons == null) {
            throw new IllegalArgumentException("Le don ne peut pas être null.");
        }
        if (dons.getDonateurId() <= 0) {
            throw new IllegalArgumentException("L'ID du donateur doit être un entier positif.");
        }
        if (dons.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        if (dons.getTypeDon() == null || dons.getTypeDon().trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de don ne peut pas être vide.");
        }
        if (dons.getDescription() == null || dons.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }
        if (dons.getStatut() == null || dons.getStatut().trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide.");
        }

        String sql = "INSERT INTO dons (donateur_id, montant, date_don, type_don, description, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, dons.getDonateurId());
            stm.setDouble(2, dons.getMontant());
            stm.setDate(3, new java.sql.Date(dons.getDateDon().getTime()));
            stm.setString(4, dons.getTypeDon());
            stm.setString(5, dons.getDescription());
            stm.setString(6, dons.getStatut());
            stm.executeUpdate();
            System.out.println("Don ajouté avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du don : " + e.getMessage());
        }

    }

    @Override
    public void modifier(Dons dons) {
        if (dons == null) {
            throw new IllegalArgumentException("Le don ne peut pas être null.");
        }
        if (dons.getId() <= 0) {
            throw new IllegalArgumentException("L'ID du don doit être un entier positif.");
        }
        if (dons.getDonateurId() <= 0) {
            throw new IllegalArgumentException("L'ID du donateur doit être un entier positif.");
        }
        if (dons.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0.");
        }

        if (dons.getTypeDon() == null || dons.getTypeDon().trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de don ne peut pas être vide.");
        }
        if (dons.getDescription() == null || dons.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }
        if (dons.getStatut() == null || dons.getStatut().trim().isEmpty()) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide.");
        }

        String sql = "UPDATE dons SET donateur_id = ?, montant = ?, date_don = ?, type_don = ?, description = ?, statut = ?";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, dons.getDonateurId());
            stm.setDouble(2, dons.getMontant());
            stm.setDate(3, new java.sql.Date(dons.getDateDon().getTime()));
            stm.setString(4, dons.getTypeDon());
            stm.setString(5, dons.getDescription());
            stm.setString(6, dons.getStatut());
            int rowsUpdated = stm.executeUpdate();

            stm.executeUpdate();
            System.out.println("Don modifié avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM dons WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Don supprimé avec succès !");
            } else {
                System.out.println("Aucun don trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du don : " + e.getMessage());
        }

    }

    @Override
    public List<Dons> getall() {
        List<Dons> donsList = new ArrayList<>();
        String sql = "SELECT * FROM dons";

        try (Statement stm = cnx.createStatement()) {
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Dons dons = new Dons();
                dons.setId(rs.getInt("id"));
                dons.setDonateurId(rs.getInt("donateur_id"));
                dons.setMontant(rs.getDouble("montant"));
                dons.setDateDon(rs.getDate("date_don"));
                dons.setTypeDon(rs.getString("type_don"));
                dons.setDescription(rs.getString("description"));
                dons.setStatut(rs.getString("statut"));

                donsList.add(dons);
            }
            System.out.println(donsList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return donsList;
    }

    @Override
    public Dons getone(int id) {
        String sql = "SELECT * FROM dons WHERE id = ?";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Dons dons = new Dons();
                dons.setId(rs.getInt("id"));
                dons.setDonateurId(rs.getInt("donateur_id"));
                dons.setMontant(rs.getDouble("montant"));
                dons.setDateDon(rs.getDate("date_don"));
                dons.setTypeDon(rs.getString("type_don"));
                dons.setDescription(rs.getString("description"));
                dons.setStatut(rs.getString("statut"));
                return dons;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du don : " + e.getMessage());
        }

        System.out.println("Aucun don trouvé avec l'ID spécifié.");
        return null;
    }
}

