package Donateur.tn.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    Connection cnx;



    public static DatabaseConnection instance;
    public DatabaseConnection(){

        String Url="jdbc:mysql://localhost/donateur";
        String Username="root";
        String Password="";

        //établit la connexion.
        //Si la connexion réussit, il affiche "Connexion établie"
        try {
            cnx= DriverManager.getConnection(Url,Username,Password);
            System.out.println("Connextion etablie");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Cette méthode garantit qu'il n'y a qu'une seule instance de DatabaseConnection
    //Si instance est null, il crée une nouvelle connexion.
    //Sinon, il renvoie l'instance existante.
    public static DatabaseConnection getInstance() {
        if(instance==null){
            instance=  new DatabaseConnection();
        }
        return instance;
    }
     //renvoie la connexion à la base de données.
    public Connection getCnx() {
        return cnx;
    }
}
