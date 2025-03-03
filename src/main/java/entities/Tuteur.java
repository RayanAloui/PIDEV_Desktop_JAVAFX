package entities;

import java.util.Objects;

public class Tuteur {
    private int idT;
    private String cinT;
    private String nomT;
    private String prenomT;
    private String telephoneT;
    private String adresseT;
    private String disponibilite;
    private String email;

    public Tuteur() {}

    public Tuteur(int idT, String nomT, String prenomT) {
        this.idT = idT;
        this.nomT = nomT;
        this.prenomT = prenomT;
    }

    public Tuteur(int idT, String nomT, String prenomT, String email , String cinT) {
        this.idT = idT;
        this.nomT = nomT;
        this.prenomT = prenomT;
        this.email = email;
        this.cinT = cinT;
    }

    public Tuteur(int idT, String cinT, String nomT, String prenomT, String telephoneT, String adresseT, String disponibilite, String email) {
        this.idT = idT;
        this.cinT = cinT;
        this.nomT = nomT;
        this.prenomT = prenomT;
        this.telephoneT = telephoneT;
        this.adresseT = adresseT;
        this.disponibilite = disponibilite;
        this.email = email;
    }

    public Tuteur(String cinT, String nomT, String prenomT, String telephoneT, String adresseT, String disponibilite, String email) {
        this.cinT = cinT;
        this.nomT = nomT;
        this.prenomT = prenomT;
        this.telephoneT = telephoneT;
        this.adresseT = adresseT;
        this.disponibilite = disponibilite;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        if (disponibilite.equals("oui") || disponibilite.equals("non")) {
            this.disponibilite = disponibilite;
        } else {
            throw new IllegalArgumentException("La disponibilite doit être 'oui' ou 'non'");
        }
    }

    @Override
    public String toString() {
        return "Tuteur{" +
                "idT=" + idT +
                ", cinT='" + cinT + '\'' +
                ", nomT='" + nomT + '\'' +
                ", prenomT='" + prenomT + '\'' +
                ", telephoneT='" + telephoneT + '\'' +
                ", adresseT='" + adresseT + '\'' +
                ", email='" + email + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                '}';
    }

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
        return Objects.hash(idT, cinT, nomT, prenomT, telephoneT, adresseT,disponibilite,email);
    }

    public boolean isValid() {
        return cinT != null && !cinT.isEmpty() && nomT != null && !nomT.isEmpty() &&
                prenomT != null && !prenomT.isEmpty() && disponibilite != null && !disponibilite.isEmpty()
                && email != null && !email.isEmpty();
    }

}

