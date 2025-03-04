package esprit.tn.entities;

import java.util.Objects;

public class Participant {
    private int id;
    private String nom;
    private String prenom;
    private int age;

    // Constructeur par défaut
    public Participant() {}

    // Constructeur avec tous les champs sauf l'id
    public Participant(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    // Constructeur avec tous les champs
    public Participant(int id, String nom, String prenom, int age) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    // Méthode equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant participant = (Participant) o;
        return id == participant.id &&
                age == participant.age &&
                Objects.equals(nom, participant.nom) &&
                Objects.equals(prenom, participant.prenom);
    }

    // Méthode hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, age);
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                '}';
    }
}

