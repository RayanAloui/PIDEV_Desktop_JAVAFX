package esprit.tn.controllers;

import esprit.tn.entities.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import esprit.tn.entities.visites;
import esprit.tn.services.VisitesService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AjouterVisite {

    @FXML
    private DatePicker date;

    @FXML
    private TextField date1;  // Contient les heures (HH)

    @FXML
    private TextField date2;  // Contient les minutes (MM)

    @FXML
    private TextField motif;

    @FXML
    private TextField statut;

    @FXML
    private TextField visiteur;



    public void initData(int visiteurId) {
        visiteur.setText(String.valueOf(visiteurId)); // 🔹 Remplir le champ avec l'ID du visiteur
        visiteur.setEditable(false); // 🔹 Désactiver la modification manuelle
    }

    @FXML
    void AjouterV(ActionEvent event) {
        try {
            // Récupération des valeurs
            LocalDate dateVisite = date.getValue();
            String heures = date1.getText().trim();
            String minutes = date2.getText().trim();
            String motifText = motif.getText().trim();
            String statutText = statut.getText().trim();
            String visiteurText = visiteur.getText().trim();

            // Vérification que tous les champs sont remplis
            if (dateVisite == null || heures.isEmpty() || minutes.isEmpty() || motifText.isEmpty() || statutText.isEmpty() || visiteurText.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis !");
            }

            // Vérification que la date n'est pas antérieure à aujourd'hui
            if (dateVisite.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La date de visite ne peut pas être antérieure à aujourd'hui !");
            }

            // Vérification que date1 et date2 sont des nombres valides
            if (!heures.matches("\\d{1,2}") || !minutes.matches("\\d{1,2}")) {
                throw new IllegalArgumentException("L'heure et les minutes doivent être des nombres !");
            }

            // Conversion des valeurs en entiers
            int heuresInt = Integer.parseInt(heures);
            int minutesInt = Integer.parseInt(minutes);

            // Vérification des plages horaires valides
            if (heuresInt < 0 || heuresInt > 23) {
                throw new IllegalArgumentException("Les heures doivent être comprises entre 0 et 23 !");
            }
            if (minutesInt < 0 || minutesInt > 59) {
                throw new IllegalArgumentException("Les minutes doivent être comprises entre 0 et 59 !");
            }

            // Création de l'heure sous format LocalTime
            LocalTime heureFormat = LocalTime.of(heuresInt, minutesInt);

            // Vérification du format du statut
            if (!statutText.matches("^(En attente|Accepté|Refusé)$")) {
                throw new IllegalArgumentException("Le statut doit être 'En attente', 'Accepté' ou 'Refusé' !");
            }

            // Conversion du champ visiteur en entier
            int visiteurId;
            try {
                visiteurId = Integer.parseInt(visiteurText);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("L'ID du visiteur doit être un nombre !");
            }


            // Création de l'objet Visite
            visites newVisite = new visites();
            newVisite.setDate(dateVisite);
            newVisite.setHeure(heureFormat);
            newVisite.setMotif(motifText);
            newVisite.setStatut(statutText);
            newVisite.setId_visiteur(visiteurId);

            // Ajout à la base de données
            VisitesService vs = new VisitesService();
            vs.ajouter(newVisite);

            // Affichage d'une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Visite Ajoutée");
            alert.setContentText("La visite a été ajoutée avec succès !");
            alert.showAndWait();

            // Retour à la page précédente
            retour(event);

        } catch (IllegalArgumentException e) {
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setTitle("Erreur de saisie");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        } catch (SQLException e) {
            Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
            sqlAlert.setContentText("Erreur SQL : " + e.getMessage());
            sqlAlert.showAndWait();
        }
    }


    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/AfficherVisite.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) date.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
