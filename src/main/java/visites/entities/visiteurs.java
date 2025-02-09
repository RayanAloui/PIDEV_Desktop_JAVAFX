package visites.entities;

import java.util.Objects;

public class visiteurs
{
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private int tel;
    private String adresse;

    public visiteurs(){};

    public visiteurs(String nom, String prenom, String email, int tel, String adresse){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
    }
    public visiteurs(int id, String nom, String prenom, String email, int tel, String adresse){
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.tel = tel;
            this.adresse = adresse;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getNom(){return nom;}

    public void setNom(String nom){this.nom = nom;}

    public String getPrenom(){return prenom;}

    public void setPrenom(String prenom){this.prenom = prenom;}

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}

    public int getTel(){return tel;}

    public void setTel(int tel){this.tel = tel;}

    public String getAdresse(){return adresse;}

    public void setAdresse(String adresse){this.adresse = adresse;}

    @Override
    public String toString(){
        return "Visiteur{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", tel=" + tel + ", adresse=" + adresse + '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass()!= o.getClass()) return false;
        visiteurs visiteur = (visiteurs) o;
        return id == visiteur.id && nom.equals(visiteur.nom) && prenom.equals(visiteur.prenom);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id,nom,prenom);
    }
}
