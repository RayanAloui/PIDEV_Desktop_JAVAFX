package entities;

import java.util.Objects;

public class Tuteur {
    private int idT;
    private String cinT;
    private String nomT;
    private String prenomT;
    private String telephoneT;
    private String adresseT;

    public Tuteur() {}

    public Tuteur(int idT, String cinT, String nomT, String prenomT, String telephoneT, String adresseT) {
        this.idT = idT;
        this.cinT = cinT;
        this.nomT = nomT;
        this.prenomT = prenomT;
        this.telephoneT = telephoneT;
        this.adresseT = adresseT;
    }

    public Tuteur(String cinT, String nomT, String prenomT, String telephoneT, String adresseT) {
        this.cinT = cinT;
        this.nomT = nomT;
        this.prenomT = prenomT;
        this.telephoneT = telephoneT;
        this.adresseT = adresseT;
    }

    // Getters et Setters
    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public String getCinT() {
        return cinT;
    }

    public void setCinT(String cinT) {
        this.cinT = cinT;
    }

    public String getNomT() {
        return nomT;
    }

    public void setNomT(String nomT) {
        this.nomT = nomT;
    }

    public String getPrenomT() {
        return prenomT;
    }

    public void setPrenomT(String prenomT) {
        this.prenomT = prenomT;
    }

    public String getTelephoneT() {
        return telephoneT;
    }

    public void setTelephoneT(String telephoneT) {
        this.telephoneT = telephoneT;
    }

    public String getAdresseT() {
        return adresseT;
    }

    public void setAdresseT(String adresseT) {
        this.adresseT = adresseT;
    }

    // Méthode toString pour afficher les informations d'un tuteur
    @Override
    public String toString() {
        return "Tuteur{" +
                "idT=" + idT +
                ", cinT='" + cinT + '\'' +
                ", nomT='" + nomT + '\'' +
                ", prenomT='" + prenomT + '\'' +
                ", telephoneT='" + telephoneT + '\'' +
                ", adresseT='" + adresseT + '\'' +
                '}';
    }

    // Méthode equals pour comparer deux tuteurs
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuteur tuteur = (Tuteur) o;
        return idT == tuteur.idT && Objects.equals(cinT, tuteur.cinT);
    }


    // Méthode hashCode pour générer un code unique basé sur les attributs
    @Override
    public int hashCode() {
        return Objects.hash(idT, cinT, nomT, prenomT, telephoneT, adresseT);
    }

    public boolean isValid() {
        return cinT != null && !cinT.isEmpty() && nomT != null && !nomT.isEmpty() &&
                prenomT != null && !prenomT.isEmpty();
    }

}

