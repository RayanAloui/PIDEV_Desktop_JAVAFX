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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class AjouterDonController {
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
    private DonateurService donateurService = new DonateurService();
    @FXML
    public void initialize() {
        chargerDonateurs();
    }
    private void chargerDonateurs() {
        List<Integer> listeIds = donateurService.getAllIds();
        System.out.println("IDs des donateurs récupérés : " + listeIds);// Récupérer les IDs des donateurs
        ObservableList<Integer> idsObservableList = FXCollections.observableArrayList(listeIds);
        Donateur_id.setItems(idsObservableList);
    }
    @FXML
    void AjouterDon(ActionEvent event) {
        try {
            // Vérifications des champs
            if (Donateur_id.getValue() == null) {
                showError("Veuillez sélectionner un donateur.");
                return;
            }
            if (date_don.getValue() == null) {
                showError("Veuillez sélectionner une date.");
                return;
            }
            if (description.getText().isEmpty()) {
                showError("Le champ 'Description' est obligatoire.");
                return;
            }
            if (type_don.getText().isEmpty()) {
                showError("Le champ 'Type du don' est obligatoire.");
                return;
            }
            if (montant.getText().isEmpty() || !montant.getText().matches("\\d+(\\.\\d{1,2})?")) {
                showError("Le montant doit être un nombre valide.");
                return;
            }
            if (statut.getText().isEmpty()) {
                showError("Le champ 'Statut' est obligatoire.");
                return;
            }

            // Création de l'objet Don
            Dons don = new Dons();
            don.setDonateurId(Donateur_id.getValue());
            don.setDateDon(date_don.getValue());
            don.setDescription(description.getText());
            don.setTypeDon(type_don.getText());
            don.setMontant(Double.parseDouble(montant.getText()));
            don.setStatut(statut.getText());

            // Ajout du don
            DonsService ds = new DonsService();
            System.out.println("Ajout du don en cours...");
            ds.ajouter(don);
            System.out.println("Don ajouté avec succès !");

            showInfo("Don ajouté avec succès !");
        } catch (NumberFormatException e) {
            showError("Le montant est invalide.");
        } catch (Exception e) {
            showError("Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDons.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Donateur_id.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AnnulerSaisie(ActionEvent event) {
        Donateur_id.setValue(null);
        date_don.setValue(null);
        description.clear();
        montant.clear();
        statut.clear();
        type_don.clear();
    }

    @FXML
    void backListDon(ActionEvent event) {
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
