package reclamations.entities;

import java.sql.Date;
import java.util.Objects;

public class Reclamation {






    private int id;
    private String mail;
    private String description;
    private Date date;
    private String statut;

    // Default constructor
    public Reclamation() {}

    // Constructor without id (for creating new entries)
    public Reclamation(String mail, String description, Date date, String statut) {
        this.mail = mail;
        this.description = description;
        this.date = date;
        this.statut = statut;
    }

    // Constructor with id (for existing entries)
    public Reclamation(int id, String mail, String description, Date date, String statut) {
        this.id = id;
        this.mail = mail;
        this.description = description;
        this.date = date;
        this.statut = statut;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    // equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamation that = (Reclamation) o;
        return id == that.id &&
                Objects.equals(mail, that.mail) &&
                Objects.equals(description, that.description) &&
                Objects.equals(date, that.date) &&
                Objects.equals(statut, that.statut);
    }

    // hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(id, mail, description, date, statut);
    }

    // toString() method
    @Override
    public String toString() {
        return "Reclamations{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", statut='" + statut + '\'' +
                '}';
    }


}
