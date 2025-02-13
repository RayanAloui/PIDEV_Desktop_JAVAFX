package controllers;

import entities.Tuteur;
import services.ServiceTuteur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AjouterTuteurController {

    @FXML
    private TextField adresseId;

    @FXML
    private TextField cinId;

    @FXML
    private TextField nomId;

    @FXML
    private TextField prenomId;

    @FXML
    private TextField telephoneId;

    @FXML
    private Label errorCin, errorNom, errorPrenom, errorTelephone, errorAdresse;

    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    @FXML
    void AjouterT(ActionEvent event) {
        // Réinitialisation des erreurs
        errorCin.setText("");
        errorNom.setText("");
        errorPrenom.setText("");
        errorTelephone.setText("");
        errorAdresse.setText("");

        // Récupération des valeurs
        String cin = cinId.getText().trim();
        String nom = nomId.getText().trim();
        String prenom = prenomId.getText().trim();
        String telephone = telephoneId.getText().trim();
        String adresse = adresseId.getText().trim();

        boolean hasError = false;

        // Vérification du CIN
        if (cin.isEmpty()) {
            errorCin.setText("Le CIN est obligatoire !");
            hasError = true;
        } else if (!cin.matches("\\d{8}")) {
            errorCin.setText("Le CIN doit contenir exactement 8 chiffres !");
            hasError = true;
        }

        // Vérification du nom
        if (nom.isEmpty()) {
            errorNom.setText("Le nom est obligatoire !");
            hasError = true;
        } else if (!nom.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
            errorNom.setText("Le nom ne doit contenir que des lettres !");
            hasError = true;
        }

        // Vérification du prénom
        if (prenom.isEmpty()) {
            errorPrenom.setText("Le prénom est obligatoire !");
            hasError = true;
        } else if (!prenom.matches("[a-zA-ZÀ-ÿ\\s-]+")) {
            errorPrenom.setText("Le prénom ne doit contenir que des lettres !");
            hasError = true;
        }

        // Vérification du téléphone
        if (!telephone.matches("\\d{8}")) {
            errorTelephone.setText("Le téléphone doit contenir exactement 8 chiffres !");
            hasError = true;
        }

        // Si une erreur est détectée, arrêter l'ajout
        if (hasError) return;

        // Création et ajout du tuteur
        try {
            Tuteur tuteur = new Tuteur(cin, nom, prenom, telephone, adresse);
            serviceTuteur.ajouter(tuteur);

            // Réinitialiser les champs après un ajout réussi
            cinId.clear();
            nomId.clear();
            prenomId.clear();
            telephoneId.clear();
            adresseId.clear();

            // Affichage d'un message de succès
            /*errorAdresse.setText("Tuteur ajouté avec succès !");
            errorAdresse.setStyle("-fx-text-fill: green;");*/

            // Afficher une alerte de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajout réussi");
            alert.setHeaderText(null);
            alert.setContentText("Le tuteur a été ajouté avec succès !");
            alert.showAndWait();

        } catch (SQLException e) {
            errorAdresse.setText("Erreur lors de l'ajout du tuteur !");
            errorAdresse.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    @FXML
    void afficherTuteur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTuteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Tuteurs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
