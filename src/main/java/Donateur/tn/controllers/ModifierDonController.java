package Donateur.tn.controllers;

import Donateur.tn.entities.Dons;
import Donateur.tn.entities.donateur;
import Donateur.tn.services.DonateurService;
import Donateur.tn.services.DonsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class ModifierDonController {

    @FXML
    private ChoiceBox<Integer> Donateur_id;

    @FXML
    private DatePicker date_don;

    @FXML
    private TextField description;

    @FXML
    private TextField montant;

    @FXML
    private TextField statut;

    @FXML
    private TextField type_don;
    private Dons donSelectionne;
    private DonateurService donateurService = new DonateurService();

    public void initialize() {
        chargerDonateurs();
    }
    private void chargerDonateurs() {
        List<Integer> listeIds = donateurService.getAllIds();
        System.out.println("IDs des donateurs récupérés : " + listeIds);// Récupérer les IDs des donateurs
        ObservableList<Integer> idsObservableList = FXCollections.observableArrayList(listeIds);
        Donateur_id.setItems(idsObservableList);
    }
    public void initDataDon(Dons don) {
        this.donSelectionne = don;

        // Pré-remplir les champs avec les données existantes
        Donateur_id.setValue(don.getDonateurId());
        date_don.setValue(don.getDateDon());
        description.setText(don.getDescription());
        type_don.setText(don.getTypeDon());
        montant.setText(String.valueOf(don.getMontant()));
        statut.setText(don.getStatut());
    }

    @FXML
    void AnnulerSaisie(ActionEvent event) {
        // Réinitialiser tous les champs
        Donateur_id.setValue(null);
        date_don.setValue(null);
        description.clear();
        type_don.clear();
        montant.clear();
        statut.clear();
    }

    @FXML
    void ModifierDon(ActionEvent event) {

        try {
            // Récupération des valeurs depuis les champs de texte
            Integer donateurIdValue = Donateur_id.getValue();
            if (donateurIdValue == null) {
                showError("Veuillez sélectionner un donateur.");
                return;  // Empêche la suite du traitement si aucun donateur n'est sélectionné
            }
            LocalDate dateDonValue = date_don.getValue();
            if (dateDonValue == null) {
                showError("Veuillez sélectionner une date.");
                return;
            }
            String descriptionValue = description.getText();
            String typeDonValue = type_don.getText();
            double montantValue = Double.parseDouble(montant.getText());
            String statutValue = statut.getText();

            // Validation des données
            if (descriptionValue.isEmpty() || typeDonValue.isEmpty() || statutValue.isEmpty() || montantValue <= 0) {
                showError("Tous les champs doivent être remplis correctement.");
                return;
            }

            // Mise à jour du don sélectionné
            donSelectionne.setDonateurId(donateurIdValue);
            donSelectionne.setDateDon(dateDonValue);
            donSelectionne.setDescription(descriptionValue);
            donSelectionne.setTypeDon(typeDonValue);
            donSelectionne.setMontant(montantValue);
            donSelectionne.setStatut(statutValue);

            // Mise à jour du don dans la base de données
            DonsService.getInstance().modifier(donSelectionne);

            // Message de confirmation
            showInfo("Don modifié avec succès.");

            // Retour à la liste des dons
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDons.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) Donateur_id.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showError("Les valeurs numériques doivent être valides.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void backList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherDons.fxml")));
            statut.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
