package esprit.tn.entities;

import java.util.Objects;

public class Orphelin {

    private int idO;
    private String nomO;
    private String prenomO;
    private String dateNaissance;
    private String sexe;
    private String situationScolaire;
    private int idTuteur;
    private Tuteur tuteur;


    public Orphelin() {}

    public Orphelin(int idO, String nomO, String prenomO, String dateNaissance, String sexe, String situationScolaire, int idTuteur) {
        this.idO = idO;
        this.nomO = nomO;
        this.prenomO = prenomO;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.situationScolaire = situationScolaire;
        this.idTuteur = idTuteur;
    }

    public Orphelin(String nomO, String prenomO, String dateNaissance, String sexe, String situationScolaire, int idTuteur) {
        this.nomO = nomO;
        this.prenomO = prenomO;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.situationScolaire = situationScolaire;
        this.idTuteur = idTuteur;
    }

    // Getters et Setters
    public Tuteur getTuteur() {
        return tuteur;
    }

    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
    }


    public int getIdO() {
        return idO;
    }

    public void setIdO(int idO) {
        this.idO = idO;
    }

    public String getNomO() {
        return nomO;
    }

    public void setNomO(String nomO) {
        this.nomO = nomO;
    }

    public String getPrenomO() {
        return prenomO;
    }

    public void setPrenomO(String prenomO) {
        this.prenomO = prenomO;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getSituationScolaire() {
        return situationScolaire;
    }

    public void setSituationScolaire(String situationScolaire) {
        this.situationScolaire = situationScolaire;
    }

    public int getIdTuteur() {
        return idTuteur;
    }

    public void setIdTuteur(int idTuteur) {
        this.idTuteur = idTuteur;
    }

    @Override
    public String toString() {
        return "Orphelin [idO=" + idO + ", nomO=" + nomO + ", prenomO=" + prenomO + ", dateNaissance=" + dateNaissance
                + ", sexe=" + sexe + ", situationScolaire=" + situationScolaire + ", idTuteur=" + idTuteur + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orphelin orphelin = (Orphelin) o;
        return idO == orphelin.idO;
    }

    // Méthode hashCode pour générer un code unique basé sur les attributs
    @Override
    public int hashCode() {
        return Objects.hash(idO, nomO, prenomO, dateNaissance, sexe, situationScolaire, idTuteur);
    }

    public boolean isValid() {
        return nomO != null && !nomO.isEmpty() &&
                prenomO != null && !prenomO.isEmpty() &&
                dateNaissance != null && !dateNaissance.isEmpty() &&
                sexe != null && !sexe.isEmpty() &&
                idTuteur > 0;
    }


}

