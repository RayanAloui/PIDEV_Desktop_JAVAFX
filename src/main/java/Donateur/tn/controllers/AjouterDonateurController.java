package Donateur.tn.controllers;

import Donateur.tn.entities.donateur;
import Donateur.tn.services.DonateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class AjouterDonateurController {
    @FXML
    private TextField adresseD;

    @FXML
    private TextField emailD;

    @FXML
    private TextField nomD;

    @FXML
    private TextField prenomD;

    @FXML
    private TextField telD;

    @FXML
    void AjouterDonateur(ActionEvent event) {
        if (nomD.getText().isEmpty()) {
            showAlert("Erreur de saisie", "Le champ 'Nom' est obligatoire.");
            return;
        }
        if (prenomD.getText().isEmpty()) {
            showAlert("Erreur de saisie", "Le champ 'Prénom' est obligatoire.");
            return;
        }
        if (emailD.getText().isEmpty() || !emailD.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showAlert("Erreur de saisie", "Le champ 'Email' est obligatoire et doit être valide.");
            return;
        }
        if (telD.getText().isEmpty() || !telD.getText().matches("\\d+")) {
            showAlert("Erreur de saisie", "Le champ 'Téléphone' est obligatoire et doit contenir uniquement des chiffres.");
            return;
        }
        if (adresseD.getText().isEmpty()) {
            showAlert("Erreur de saisie", "Le champ 'Adresse' est obligatoire.");
            return;
        }
        //Crée un objet donateur et lui attribue les valeurs saisies.
        try {
        donateur dt = new donateur();
        dt.setNom(nomD.getText());
        dt.setPrenom(prenomD.getText());
        dt.setEmail(emailD.getText());
        dt.setTelephone(Integer.parseInt(telD.getText()));
        dt.setAdresse(adresseD.getText());
        //Ajoute le donateur dans la base de données via DonateurService
        DonateurService ds = new DonateurService();
        ds.ajouter(dt);


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDonateur.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) nomD.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            showAlert("Ajout Donateur", "Donateur ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le numéro de téléphone est invalide.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du donateur.");
            e.printStackTrace();
        }
    }

    @FXML
    void AnnulerSaisie(ActionEvent event) {
        nomD.clear();
        prenomD.clear();
        emailD.clear();
        telD.clear();
        adresseD.clear();

    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void backList(ActionEvent event)  {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherDonateur.fxml")));
            nomD.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
