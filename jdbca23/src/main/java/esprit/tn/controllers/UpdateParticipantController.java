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

public class UpdateParticipantController {

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

    private Participant selectedParticipant;

    @FXML
    private void initialize() {
        loadSelectedParticipant();
    }

    private void loadSelectedParticipant() {
        selectedParticipant = AfficherParticipantController.getSelectedParticipant();
        if (selectedParticipant != null) {
            nom.setText(selectedParticipant.getNom());
            prenom.setText(selectedParticipant.getPrenom());
            age.setText(String.valueOf(selectedParticipant.getAge()));
        }
    }

    @FXML
    private void UpdateParticipant() {
        resetErrors();
        boolean isValid = true;

        if (nom.getText().isEmpty()) {
            nomError.setVisible(true);
            isValid = false;
        }
        if (prenom.getText().isEmpty()) {
            prenomError.setVisible(true);
            isValid = false;
        }
        if (age.getText().isEmpty()) {
            ageError.setVisible(true);
            isValid = false;
        }

        if (isValid) {
            selectedParticipant.setNom(nom.getText());
            selectedParticipant.setPrenom(prenom.getText());
            selectedParticipant.setAge(Integer.parseInt(age.getText()));

            participantService.modifier(selectedParticipant, selectedParticipant.getId());
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

