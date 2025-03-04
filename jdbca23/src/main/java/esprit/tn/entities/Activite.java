package esprit.tn.entities;

import java.util.Objects;

public class Activite {
    private int id;
    private String nom;
    private String categorie;
    private String duree;
    private String nom_du_tuteur;
    private String date_activite;
    private String lieu;
    private String description;
    private String statut;

    // Constructeur par défaut
    public Activite() {}

    // Constructeur avec tous les champs sauf l'id
    public Activite(String nom, String categorie, String duree, String nom_du_tuteur, String date_activite, String lieu, String description, String statut) {
        this.nom = nom;
        this.categorie = categorie;
        this.duree = duree;
        this.nom_du_tuteur = nom_du_tuteur;
        this.date_activite = date_activite;
        this.lieu = lieu;
        this.description = description;
        this.statut = statut;
    }

    // Constructeur avec tous les champs
    public Activite(int id, String nom, String categorie, String duree, String nom_du_tuteur, String date_activite, String lieu, String description, String statut) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.duree = duree;
        this.nom_du_tuteur = nom_du_tuteur;
        this.date_activite = date_activite;
        this.lieu = lieu;
        this.description = description;
        this.statut = statut;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }

    public String getNom_du_tuteur() { return nom_du_tuteur; }
    public void setNom_du_tuteur(String nom_du_tuteur) { this.nom_du_tuteur = nom_du_tuteur; }

    public String getDate_activite() { return date_activite; }
    public void setDate_activite(String date_activite) { this.date_activite = date_activite; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    // Méthode equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activite)) return false;
        Activite activite = (Activite) o;
        return id == activite.id &&
                Objects.equals(nom, activite.nom) &&
                Objects.equals(categorie, activite.categorie) &&
                Objects.equals(duree, activite.duree) &&
                Objects.equals(nom_du_tuteur, activite.nom_du_tuteur) &&
                Objects.equals(date_activite, activite.date_activite) &&
                Objects.equals(lieu, activite.lieu) &&
                Objects.equals(description, activite.description) &&
                Objects.equals(statut, activite.statut);
    }

    // Méthode hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, nom, categorie, duree, nom_du_tuteur, date_activite, lieu, description, statut);
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Activite{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", duree='" + duree + '\'' +
                ", nom_du_tuteur='" + nom_du_tuteur + '\'' +
                ", date_activite='" + date_activite + '\'' +
                ", lieu='" + lieu + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}