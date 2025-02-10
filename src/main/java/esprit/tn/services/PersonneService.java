package esprit.tn.services;

import esprit.tn.entities.Personne;
import esprit.tn.main.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneService implements Iservice<Personne> {

    Connection cnx;

   public PersonneService(){

       cnx= DatabaseConnection.instance.getCnx();
   }

  @Override
  public void ajouter(Personne personne) {
      String req="INSERT INTO  `personne`( `nom`, `prenom`) VALUES ('" + personne.getNom() + "','" + personne.getPrenom() + "')";
       try {
           Statement stm= cnx.createStatement();
           stm.executeUpdate(req);
        } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }
    @Override
    public void modifier(Personne personne,int id) {
        System.out.println('a');

    }

    @Override
    public void supprimer(int id) {

    }

    @Override
    public List<Personne> getall() {
       List<Personne> ps=new ArrayList<>();

       String req="SELECT * FROM personne";

        try {
            Statement stm=cnx.createStatement();
            ResultSet rs= stm.executeQuery(req);

            while (rs.next()){
                Personne p=new Personne();
              p.setId( rs.getInt("Id"));
              p.setNom(  rs.getString("Nom"));
                p.setPrenom(rs.getString("Prenom"));
                ps.add(p);
            }

            System.out.println(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ps;
    }

    @Override
    public Personne getone(int id) {
        return null;
    }

    @Override
    public Personne getoneByEmail(String email) {
        return null;
    }


}
