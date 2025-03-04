package esprit.tn.controllers;

import esprit.tn.entities.Participant;
import esprit.tn.services.ParticipantService;
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

public class AddParticipantController {

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField age;

    @FXML
    private Label nomError;

    @FXML
    private Label prenomError;

    @FXML
    private Label ageError;

    private ParticipantService participantService = new ParticipantService();

    @FXML
    private void addParticipant() {
        // Reset error messages
        resetErrors();

        // Validate inputs
        boolean isValid = true;

        if (nom.getText().isEmpty()) {
            nomError.setVisible(true);
            isValid = false;
        }
        if (prenom.getText().isEmpty()) {
            prenomError.setVisible(true);
            isValid = false;
        }
        if (age.getText().isEmpty() || !age.getText().matches("\\d+")) {
            ageError.setVisible(true);
            isValid = false;
        }

        if (isValid) {
            // Create new Participant object
            Participant participant = new Participant(
                    nom.getText(),
                    prenom.getText(),
                    Integer.parseInt(age.getText())
            );

            // Add the participant to the database
            participantService.ajouter(participant);

            // Redirect to the "AfficherParticipant" screen
            GoToAfficherParticipant();
        }
    }

    @FXML
    private void GoToAfficherParticipant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) nom.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AfficherParticipant.fxml");
        }
    }

    private void resetErrors() {
        nomError.setVisible(false);
        prenomError.setVisible(false);
        ageError.setVisible(false);
    }
}

