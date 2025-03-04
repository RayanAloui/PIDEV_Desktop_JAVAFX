package esprit.tn.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

public class Notification {
    private int id;
    private Date date;
    private String heure;
    private String username;
    private String activite;

    // Constructor
    public Notification(int id, Date date, String heure, String username, String activite) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.username = username;
        this.activite = activite;
    }

    public Notification() {

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date; // Return the stored date, not the current date
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    // toString method
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", date=" + date +
                ", heure='" + heure + '\'' +
                ", username='" + username + '\'' +
                ", activite='" + activite + '\'' +
                '}';
    }
}
