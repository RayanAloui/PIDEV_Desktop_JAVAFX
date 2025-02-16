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


}
