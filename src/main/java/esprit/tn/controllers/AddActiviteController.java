package esprit.tn.controllers;

import esprit.tn.entities.Activite;
import esprit.tn.services.ActiviteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddActiviteController {

    @FXML
    private TextField nom;

    @FXML
    private ChoiceBox<String> categorie;

    @FXML
    private TextField duree;

    @FXML
    private TextField nomTuteur;

    @FXML
    private TextField dateActivite;

    @FXML
    private TextField lieu;

    @FXML
    private TextArea description;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label nomError;

    @FXML
    private Label categorieError;

    @FXML
    private Label dureeError;

    @FXML
    private Label nomTuteurError;

    @FXML
    private Label dateActiviteError;

    @FXML
    private Label lieuError;

    @FXML
    private Label descriptionError;

    @FXML
    private Label statutError;

    private ActiviteService activiteService = new ActiviteService();

    @FXML
    private void addActivite() {
        // Reset error messages
        resetErrors();

        // Validate inputs
        boolean isValid = true;

        if (nom.getText().isEmpty()) {
            nomError.setVisible(true);
            isValid = false;
        }
        if (categorie.getValue() == null) {
            categorieError.setVisible(true);
            isValid = false;
        }
        if (duree.getText().isEmpty()) {
            dureeError.setVisible(true);
            isValid = false;
        }
        if (nomTuteur.getText().isEmpty()) {
            nomTuteurError.setVisible(true);
            isValid = false;
        }
        if (dateActivite.getText().isEmpty()) {
            dateActiviteError.setVisible(true);
            isValid = false;
        }
        if (lieu.getText().isEmpty()) {
            lieuError.setVisible(true);
            isValid = false;
        }
        if (description.getText().isEmpty()) {
            descriptionError.setVisible(true);
            isValid = false;
        }
        if (statut.getValue() == null) {
            statutError.setVisible(true);
            isValid = false;
        }

        if (isValid) {
            // Create new Activite object
            Activite activite = new Activite(
                    nom.getText(),
                    categorie.getValue(),
                    duree.getText(),
                    nomTuteur.getText(),
                    dateActivite.getText(),
                    lieu.getText(),
                    description.getText(),
                    statut.getValue()
            );

            // Add the activity to the database
            activiteService.ajouter(activite);

            // Redirect to the "AfficherActivite" screen
            GoToAfficherActivite();
        }
    }

    @FXML
    private void GoToAfficherActivite() {
        try {
            // Load the AfficherActivite.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherActivite.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) nom.getScene().getWindow(); // Replace 'nom' with any component in the current scene

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AfficherActivite.fxml");
        }
    }

    private void resetErrors() {
        nomError.setVisible(false);
        categorieError.setVisible(false);
        dureeError.setVisible(false);
        nomTuteurError.setVisible(false);
        dateActiviteError.setVisible(false);
        lieuError.setVisible(false);
        descriptionError.setVisible(false);
        statutError.setVisible(false);
    }
}