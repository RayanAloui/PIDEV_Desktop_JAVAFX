package esprit.tn.services;

import esprit.tn.entities.Notification;
import esprit.tn.entities.User;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private Connection cnx;

    public NotificationService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification ORDER BY date DESC, heure DESC"; 

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("heure"),
                        rs.getString("username"),
                        rs.getString("activite")
                );
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // You can also log the exception if needed
        }

        return notifications;
    }




    public void ajouter(Notification n) {
        String req = "INSERT INTO notification (date, heure, username, activite) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            stm.setDate(1, n.getDate());
            stm.setString(2, n.getHeure());
            stm.setString(3, n.getUsername());
            stm.setString(4, n.getActivite());

            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Log or rethrow as necessary
            throw new RuntimeException("Error inserting notification", e);
        }
    }


}
