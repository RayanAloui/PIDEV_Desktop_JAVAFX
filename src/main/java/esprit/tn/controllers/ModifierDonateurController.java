package esprit.tn.controllers;

import esprit.tn.entities.donateur;
import esprit.tn.services.DonateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;


public class ModifierDonateurController {

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
    private donateur donateurSelectionne;



    public void initData(donateur donateur) {
        this.donateurSelectionne = donateur;

        // Pré-remplir les champs avec les données existantes
        nomD.setText(donateur.getNom());
        prenomD.setText(donateur.getPrenom());
        emailD.setText(donateur.getEmail());
        telD.setText(String.valueOf(donateur.getTelephone()));
        adresseD.setText(donateur.getAdresse());
    }
    @FXML
    void AnnulerSaisie(ActionEvent event) {
        nomD.clear();
        prenomD.clear();
        emailD.clear();
        telD.clear();
        adresseD.clear();

    }


    @FXML
    void ModifierDonateur(ActionEvent event) {
        try {
            // Récupération des valeurs depuis les champs de texte
            String nomValue = nomD.getText();
            String prenomValue = prenomD.getText();
            String emailValue = emailD.getText();
            int telValue = Integer.parseInt(telD.getText());
            String adresseValue = adresseD.getText();

            // Validation des données
            if (nomValue.isEmpty() || prenomValue.isEmpty() || emailValue.isEmpty() || adresseValue.isEmpty() || telValue <= 0) {
                showError("Tous les champs doivent être remplis correctement.");
                return;
            }

            // Création du donateur modifié
            donateurSelectionne.setNom(nomValue);
            donateurSelectionne.setPrenom(prenomValue);
            donateurSelectionne.setEmail(emailValue);
            donateurSelectionne.setTelephone(telValue);
            donateurSelectionne.setAdresse(adresseValue);

            // Mise à jour du donateur dans la base de données
            DonateurService.getInstance().modifier(donateurSelectionne);


            // Message de confirmation
            showInfo("Donateur modifié avec succès.");

            // Retour à la liste des donateurs
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDonateur.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) nomD.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showError("Le numéro de téléphone doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }





    }

    @FXML
    void backList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherDonateur.fxml")));
            nomD.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
