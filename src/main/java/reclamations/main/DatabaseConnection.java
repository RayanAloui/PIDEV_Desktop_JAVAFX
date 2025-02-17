package reclamations.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection cnx;
    private static DatabaseConnection instance;

    // Update with your MySQL credentials
    private static final String URL = "jdbc:mysql://localhost:3306/reclamations";
    private static final String USERNAME = "root"; // Replace with your MySQL username
    private static final String PASSWORD = ""; // Replace with your MySQL password

    // Private constructor for Singleton
    private DatabaseConnection() {
        try {
            // Ensure the MySQL JDBC driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Try establishing a connection
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection established successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            // Log the error and handle exceptions more gracefully
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Singleton method to get the instance of DatabaseConnection
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Method to get the connection object
    public Connection getCnx() {
        return cnx;
    }
}
