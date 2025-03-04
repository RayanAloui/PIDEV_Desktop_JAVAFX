package reclamations.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker; // Added import for DatePicker
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;  // Added import for TextField
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import reclamations.entities.Reclamation; // Added import for Reclamation class
import reclamations.services.ReclamationService;

import reclamations.entities.Reponse;
import reclamations.services.ReponseService;

public class updatereponseController {

    @FXML
    private TextArea description;

    @FXML
    private DatePicker datePicker; // DatePicker to handle date input

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label descriptionError;

    @FXML
    private Label dateError; // For date validation

    @FXML
    private Label statutError;

    private Reponse selectedReponse; // Reference to the selected Reponse object

    public void setReponseData(Reponse reponse) {
        this.selectedReponse = reponse; // Set the selected reponse data
        description.setText(reponse.getDescription()); // Set the description
        datePicker.setValue(reponse.getDate().toLocalDate()); // Set the date from the selected Reponse
        statut.setValue(reponse.getStatut()); // Set the status of the response
    }

    @FXML
    public void GoToAfficherreponse(ActionEvent actionEvent) { // Navigate to the response list page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the reponse page");
            alert.showAndWait();
        }
    }
    @FXML
    public void Updatereponse(ActionEvent event) {
        descriptionError.setVisible(false);
        dateError.setVisible(false); // Hide date error
        statutError.setVisible(false);

        boolean isValid = true;

        // Validate description
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        // Validate date
        if (datePicker.getValue() == null) {
            dateError.setText("Date is required");
            dateError.setVisible(true);
            isValid = false;
        }

        // Validate statut (status)
        if (statut.getValue() == null) {
            statutError.setText("Status is required");
            statutError.setVisible(true);
            isValid = false;
        }

        // Stop execution if any field is invalid
        if (!isValid) {
            return;
        }

        if (selectedReponse != null) {
            // Update the selected response with new data
            selectedReponse.setDescription(description.getText());
            selectedReponse.setDate(Date.valueOf(datePicker.getValue())); // Set the selected date
            selectedReponse.setStatut(statut.getValue());

            ReponseService reponseService = new ReponseService(); // Service to handle the update operation
            reponseService.modifier(selectedReponse); // Call the service to update the response

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Response updated successfully");
            alert.showAndWait();

            // Navigate back to the response list page
            GoToAfficherreponse(event);
        } else {
            // Show warning if no response is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No response selected");
            alert.showAndWait();
        }
    }
}
