package esprit.tn.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //instance : Une variable statique pour stocker l'instance unique de DatabaseConnection.
    private static DatabaseConnection instance;
    private Connection cnx;
    private final String url = "jdbc:mysql://localhost:3306/donateur";
    private final String user = "root";
    private final String password = "";

    private DatabaseConnection() {
        ouvrirConnexion();
    }

    private void ouvrirConnexion() {
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        try {
            if (cnx == null || cnx.isClosed()) {
                System.out.println("Connexion fermée, tentative de reconnexion...");
                ouvrirConnexion();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de la connexion : " + e.getMessage());
        }
        return cnx;
    }
}
