package esprit.tn.entities;

import java.sql.Date;
import java.util.Objects;

public class Reponse {

    private int id;
    private String description;
    private Date date;
    private String statut;
    private int indice;
// Foreign key reference to Reclamation

    // Default constructor
    public Reponse() {}

    // Constructor without id (for creating new entries)
    public Reponse(String description, Date date, String statut , int indice) {
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.indice = indice;

    }

    // Constructor with id (for existing entries)
    public Reponse(int id, String description, Date date, String statut , int indice ) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.indice = indice;


    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    public int getIndice() {  return indice;  }
    public void setIndice(int indice) {this.indice = indice;  }


    // equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reponse that = (Reponse) o;
        return id == that.id &&

                Objects.equals(description, that.description) &&
                Objects.equals(date, that.date) &&
                Objects.equals(statut, that.statut);
    }

    // hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(id, description, date, statut);
    }

    // toString() method
    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", statut='" + statut + '\'' +

                '}';
    }
}
