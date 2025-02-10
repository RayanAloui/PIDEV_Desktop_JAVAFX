package esprit.tn.entities;

import java.util.Objects;

public class Personne {


    private int Id;
    private String Nom;
    private String Prenom;


   public Personne(){};

    public Personne(int id, String nom, String prenom) {
        Id = id;
        Nom = nom;
        Prenom = prenom;
    }


    public Personne(String nom, String prenom) {
        Nom = nom;
        Prenom = prenom;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personne personne)) return false;
        return Id == personne.Id && Objects.equals(Nom, personne.Nom) && Objects.equals(Prenom, personne.Prenom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Nom, Prenom);
    }


    @Override
    public String toString() {
        return "Personne{" +
                "Id=" + Id +
                ", Nom='" + Nom + '\'' +
                ", Prenom='" + Prenom + '\'' +
                '}';
    }
}
