package esprit.tn.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Dons {
    private int id;
    private int donateur_id;
    private double montant;
    private LocalDate date_don;
    private String type_don;
    private String description;
    private String statut;

    public Dons(){};

    public Dons(int id, int donateur_id, double montant, LocalDate date_don, String type_don, String description, String statut) {
        this.id = id;
        this.donateur_id = donateur_id;
        this.montant = montant;
        this.date_don = date_don;
        this.type_don = type_don;
        this.description = description;
        this.statut = statut;
    }

    public Dons(int donateur_id, double montant, LocalDate date_don, String type_don, String description, String statut){
        this.donateur_id = donateur_id;
        this.montant = montant;
        this.date_don = date_don;
        this.type_don = type_don;
        this.description = description;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonateurId() {
        return donateur_id;
    }

    public void setDonateurId(int donateurId) {
        this.donateur_id = donateurId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDateDon() {
        return date_don;
    }

    public void setDateDon(LocalDate dateDon) {
        this.date_don = dateDon;
    }

    public String getTypeDon() {
        return type_don;
    }

    public void setTypeDon(String typeDon) {
        this.type_don = typeDon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Dons dons = (Dons) o;
        return id == dons.id && donateur_id == dons.donateur_id && Double.compare(montant, dons.montant) == 0 && Objects.equals(date_don, dons.date_don) && Objects.equals(type_don, dons.type_don) && Objects.equals(description, dons.description) && Objects.equals(statut, dons.statut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, donateur_id, montant, date_don, type_don, description, statut);
    }

    @Override
    public String toString() {
        return "Dons{" +
                "id=" + id +
                ", donateurId=" + donateur_id +
                ", montant=" + montant +
                ", dateDon=" + date_don +
                ", typeDon='" + type_don + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
