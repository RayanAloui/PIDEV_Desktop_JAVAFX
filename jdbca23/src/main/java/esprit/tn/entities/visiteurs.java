package esprit.tn.entities;

import java.util.Objects;

public class visiteurs {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private int tel;
    private String adresse;
    private String cin; // 🔹 CIN est maintenant une chaîne (String)

    public visiteurs() {}

    public visiteurs(String nom, String prenom, String email, int tel, String adresse, String cin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.cin = cin; // 🔹 Stockage sous forme de chaîne
    }

    public visiteurs(int id, String nom, String prenom, String email, int tel, String adresse, String cin) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.cin = cin;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTel() { return tel; }
    public void setTel(int tel) { this.tel = tel; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCin() { return cin; }  // 🔹 Getter CIN
    public void setCin(String cin) { this.cin = cin; }  // 🔹 Setter CIN

    @Override
    public String toString() {
        return "Visiteur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", tel=" + tel +
                ", adresse='" + adresse + '\'' +
                ", cin='" + cin + '\'' +  // 🔹 Ajout du CIN sous forme de chaîne
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        visiteurs visiteur = (visiteurs) o;
        return id == visiteur.id && Objects.equals(cin, visiteur.cin); // 🔹 Comparaison basée sur ID et CIN
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cin);  // 🔹 Hash basé sur ID et CIN
    }
}
