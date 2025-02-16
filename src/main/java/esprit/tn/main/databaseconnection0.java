package esprit.tn.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection0 {
    private static final String URL = "jdbc:mysql://localhost/esprit";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion réussie !");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Erreur de connexion à la base de données !");
            e.printStackTrace();
        }
        return connection;
    }
}


