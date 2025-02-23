package esprit.tn.services;

import esprit.tn.entities.User;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService implements Iservice<User> {
    Connection cnx;

    public UserService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(User user) {
        String req = "INSERT INTO user (name, surname, telephone, email, password, role, isBlocked, isConfirmed, numberVerification , token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, user.getName());
            stm.setString(2, user.getSurname());
            stm.setString(3, user.getTelephone());
            stm.setString(4, user.getEmail());
            stm.setString(5, user.getPassword());
            stm.setString(6, user.getRole());
            stm.setInt(7, user.isBlocked());
            stm.setInt(8, user.isConfirmed());
            stm.setInt(9, user.getNumberVerification());
            stm.setInt(10, user.getToken());

            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(User user, int id) {

        String req = "UPDATE user SET name=?, surname=?, telephone=?, email=?, password=?, role=?, isBlocked=?, isConfirmed=?, numberVerification=? , token=? WHERE id=?";

        try {

            PreparedStatement stm = cnx.prepareStatement(req);


            stm.setString(1, user.getName());
            stm.setString(2, user.getSurname());
            stm.setString(3, user.getTelephone());
            stm.setString(4, user.getEmail());
            stm.setString(5, user.getPassword());
            stm.setString(6, user.getRole());
            stm.setInt(7, user.isBlocked());
            stm.setInt(8, user.isConfirmed());
            stm.setInt(9, user.getNumberVerification());


            stm.setInt(10, user.getToken());
            stm.setInt(11, id);

          
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM user WHERE id=?";
        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<User> getall(String filter) {
        List<User> users = new ArrayList<>();
        String req;

        // List of valid roles
        List<String> roles = Arrays.asList("admin", "client", "visiteur", "tuteur", "donateur");

        if (filter == null) {
            req = "SELECT * FROM user";
        } else if (roles.contains(filter)) {
            req = "SELECT * FROM user WHERE role = '" + filter + "'";
        } else {
            req = "SELECT * FROM user WHERE name = '" + filter + "' OR email = '" + filter + "' OR surname = '" + filter + "'";
        }

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("isBlocked"),
                        rs.getInt("isConfirmed"),
                        rs.getInt("numberVerification"),
                        rs.getInt("token")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


    @Override
    public User getone(int id) {
        String req = "SELECT * FROM user WHERE id=?";
        User user = null;

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("isBlocked"),
                        rs.getInt("isConfirmed"),
                        rs.getInt("numberVerification"),
                        rs.getInt("token")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }


    @Override
    public User getoneByEmail(String email) {
        String req = "SELECT * FROM user WHERE email=?";
        User user = null;

        try {
            PreparedStatement stm = cnx.prepareStatement(req);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("isBlocked"),
                        rs.getInt("isConfirmed"),
                        rs.getInt("numberVerification"),
                        rs.getInt("token")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public boolean updatePassword(int userId, String newPassword) {
        // Assuming you have a DatabaseConnection class to interact with the database
        String query = "UPDATE user SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getCnx();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Set the new password and user ID
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // If rows were affected, the update was successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // If an error occurs, return false
        }
    }


    public boolean emailExists(String email) {
        String query = "SELECT id FROM user WHERE email = ?"; // Fetching the ID instead of COUNT(*)

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) { // Use cnx instead of creating a new connection
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve the ID of the found user
                return getone(userId) != null; // Check if the user exists using getone method
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error properly in a real application
        }
        return false; // Return false if no user is found or an error occurs
    }

    public boolean telephoneExists(String phoneNumber) {
        String query = "SELECT id FROM user WHERE telephone = ?"; // Fetching the ID based on the phone number

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) { // Use existing connection (cnx)
            pstmt.setString(1, phoneNumber); // Set the phone number to the query
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve the ID of the found user
                return getone(userId) != null; // Check if the user exists using the getone method
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error properly in a real application
        }
        return false; // Return false if no user is found or an error occurs
    }
    public User telephoneExists1(String phoneNumber) {
        String query = "SELECT id FROM user WHERE telephone = ?"; // Fetching the ID based on the phone number

        try (PreparedStatement pstmt = cnx.prepareStatement(query)) { // Use existing connection (cnx)
            pstmt.setString(1, phoneNumber); // Set the phone number to the query
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve the ID of the found user

                // Now fetch the full user details based on the user ID
                String req = "SELECT * FROM user WHERE id=?";
                try (PreparedStatement stm = cnx.prepareStatement(req)) {
                    stm.setInt(1, userId);
                    ResultSet resultSet = stm.executeQuery();

                    if (resultSet.next()) {
                        return new User(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getString("telephone"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("role"),
                                resultSet.getInt("isBlocked"),
                                resultSet.getInt("isConfirmed"),
                                resultSet.getInt("numberVerification"),
                                resultSet.getInt("token")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log error properly in a real application
        }
        return null; // Return null if no user is found or an error occurs
    }

    public void BLOCK(int userId) {
        String query = "UPDATE user SET isBlocked = 1 WHERE id = ?"; // Set isBlocked to 1 for blocking the user

        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error blocking user", e);
        }
    }

    public void active(int userId) {
        String query = "UPDATE user SET isBlocked = 0 WHERE id = ?"; // Set isBlocked to 0 for activating the user

        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error activating user", e);
        }
    }
    public void confirm(int userId) {
        String query = "UPDATE user SET isConfirmed= 1 WHERE id = ?"; // Set isBlocked to 1 for blocking the user

        try {
            PreparedStatement stm = cnx.prepareStatement(query);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error blocking user", e);
        }
    }






}
