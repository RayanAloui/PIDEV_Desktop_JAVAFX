package esprit.tn.entities;

import java.util.Objects;

public class donateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private int telephone;
    private String  adresse;

    public donateur(){};

    public donateur(int id, String nom, String prenom, String email, int telephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public donateur( String nom, String prenom, String email, int telephone,String adresse){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;

    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public int getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        donateur donateur = (donateur) o;
        return id == donateur.id && telephone == donateur.telephone && Objects.equals(nom, donateur.nom) && Objects.equals(prenom, donateur.prenom) && Objects.equals(email, donateur.email) && Objects.equals(adresse, donateur.adresse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, email, telephone, adresse);
    }

    @Override
    public String toString() {
        return "donateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone=" + telephone +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
