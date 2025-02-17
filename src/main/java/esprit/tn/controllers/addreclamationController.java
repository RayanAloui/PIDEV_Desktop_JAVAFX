package esprit.tn.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;

public class addreclamationController {

    @FXML
    private TextArea description;

    @FXML
    private Label descriptionError;

    @FXML
    private TextField mail;

    @FXML
    private Label mailError;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label statutError;

    @FXML
    public void initialize() {
        // Initialize error labels to be invisible by default
        descriptionError.setVisible(false);
        mailError.setVisible(false);
        statutError.setVisible(false);

        // Set default text for error labels (optional)
        descriptionError.setText("Description is required");
        mailError.setText("Email is required");
        statutError.setText("Statut is required");

        // Clear any existing text in input fields (optional)
        description.clear();
        mail.clear();
        statut.getSelectionModel().clearSelection();
    }

    @FXML
    void GoToAfficherReclamations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreclamtions.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the reclamation page");
            alert.showAndWait();
        }
    }

    @FXML
    void addReclamation(ActionEvent event) {
       
        boolean isValid = true;

        // Reset error messages
        mailError.setVisible(false);
        descriptionError.setVisible(false);
        statutError.setVisible(false);

        // Validate email
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (mail.getText().trim().isEmpty() || !mail.getText().trim().matches(emailPattern)) {
            mailError.setText("Email must be in the format: example@example.com");
            mailError.setVisible(true);
            isValid = false;
        }

        // Validate description
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        // Validate statut
        if (statut.getValue() == null) {
            statutError.setText("Statut is required");
            statutError.setVisible(true);
            isValid = false;
        }

        // If validation fails, stop further execution
        if (!isValid) {
            return;
        }

        // Create a new Reclamation object
        Reclamation reclamation = new Reclamation();
        reclamation.setMail(mail.getText());
        reclamation.setDescription(description.getText());
        reclamation.setStatut(statut.getValue());
        reclamation.setDate(Date.valueOf(LocalDate.now()));

        // Add reclamation to the database
        ReclamationService reclamationService = new ReclamationService();
        reclamationService.ajouter(reclamation);

        // Show success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reclamation Added");
        alert.setHeaderText("Reclamation added successfully");
        alert.showAndWait();

        // Redirect to the reclamation list page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReclamations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load the reclamation list page");
            errorAlert.showAndWait();
        }
    }
}