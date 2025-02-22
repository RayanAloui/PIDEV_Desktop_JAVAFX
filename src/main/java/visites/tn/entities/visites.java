package visites.tn.entities;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
public class visites {
    private int id;
    private int id_visiteur;
    private LocalDate date;
    private LocalTime heure;
    private String motif;
    private String statut;

    public visites(){}

    public visites(LocalDate date, LocalTime heure, String motif,String statut){
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.statut = statut;
    }
    public visites(int id, int id_visiteur,LocalDate date, LocalTime heure, String motif,String statut){
        this.id = id;
        this.id_visiteur = id_visiteur;
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.statut = statut;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId_visiteur() {
        return id_visiteur;
    }
    public void setId_visiteur(int id_visiteur) {
        this.id_visiteur = id_visiteur;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getHeure() {
        return heure;
    }
    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }
    public String getMotif() {
        return motif;
    }
    public void setMotif(String motif) {
        this.motif = motif;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        visites visites = (visites) o;
        return id == visites.id &&
                id_visiteur == visites.id_visiteur &&
                Objects.equals(date, visites.date) &&
                Objects.equals(heure, visites.heure) &&
                Objects.equals(motif, visites.motif) &&
                Objects.equals(statut, visites.statut);

    }
    @Override
    public int hashCode() {
        return Objects.hash(id, id_visiteur, date, heure, motif, statut);
    }
    @Override
    public String toString() {
        return "visites{" +
                "id=" + id +
                ", id_visiteur=" + id_visiteur +
                ", date=" + date +
                ", heure=" + heure +
                ", motif='" + motif + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
