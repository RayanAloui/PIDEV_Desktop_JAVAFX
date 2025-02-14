package visites.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import visites.entities.visites;
import visites.services.VisitesService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ModifierVisite {

    @FXML
    private DatePicker date;

    @FXML
    private TextField date1;

    @FXML
    private TextField date2;

    @FXML
    private TextField motif;

    @FXML
    private TextField statut;

    @FXML
    private TextField visiteur;

    private visites visite; // Ajout de l'attribut visite pour récupérer les données de la visite sélectionnée

    // Méthode pour initialiser les champs avec les données de la visite
    public void setVisite(visites visite) {
        this.visite = visite;
        date.setValue(visite.getDate()); // Initialisation de la date
        date1.setText(String.valueOf(visite.getHeure().getHour())); // Heure
        date2.setText(String.valueOf(visite.getHeure().getMinute())); // Minute
        motif.setText(visite.getMotif());
        statut.setText(visite.getStatut());
    }

    @FXML
    void ModifierV(ActionEvent event) {
        try {
            // Récupération des valeurs depuis les champs de texte
            LocalDate dateValue = date.getValue();
            int heure = Integer.parseInt(date1.getText());  // Heure
            int minute = Integer.parseInt(date2.getText()); // Minute
            String motifValue = motif.getText();
            String statutValue = statut.getText();
            String visiteurValue = visiteur.getText();

            // Validation des données
            if (dateValue == null || heure < 0 || heure > 23 || minute < 0 || minute > 59 || motifValue.isEmpty() || statutValue.isEmpty()) {
                showError("Tous les champs doivent être remplis correctement.");
                return;
            }

            // Conversion de visiteur en int
            int visiteurId = Integer.parseInt(visiteurValue);

            // Création de l'heure avec les données saisies
            LocalTime heureVisite = LocalTime.of(heure, minute);

            // Mise à jour de la visite avec les nouvelles données
            visite.setId_visiteur(visiteurId);
            visite.setDate(dateValue);
            visite.setHeure(heureVisite);
            visite.setMotif(motifValue);
            visite.setStatut(statutValue);

            // Mise à jour de la visite dans la base de données
            VisitesService.getInstance().modifier(visite);

            // Message de confirmation
            showInfo("Visite modifiée avec succès.");

            // Retour à la liste des visites
            retour(event);

        } catch (NumberFormatException e) {
            showError("L'heure et la minute doivent être des nombres valides.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
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

    // Méthode pour afficher un message d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher un message d'information
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
