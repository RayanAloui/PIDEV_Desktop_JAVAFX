package esprit.tn.controllers;

import esprit.tn.services.ITuteurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

import esprit.tn.entities.Reclamation; // Added import for Reclamation class


public class updaterclamationController {

    @FXML
    private TextArea description;

    @FXML
    private TextField mail;

    @FXML
    private ChoiceBox<String> typereclamation;

    @FXML
    private DatePicker datePicker; // Added DatePicker for selecting the date

    @FXML
    private Label descriptionError;

    @FXML
    private Label mailError;

    @FXML
    private Label typereclamationError;

    @FXML
    private Label dateError; // Added date error label

    private Reclamation selectedReclamation;

    public void setReclamationData(Reclamation reclamation) {
        this.selectedReclamation = reclamation;
        description.setText(reclamation.getDescription());
        mail.setText(reclamation.getMail());
        typereclamation.setValue(reclamation.getTypereclamation());

        // Set the date picker with the reclamation's date
        datePicker.setValue(reclamation.getDate().toLocalDate());
    }

    @FXML
    public void GoToAfficherreclamation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreclamtions.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
    public void Updatereclamation(ActionEvent event) {
        descriptionError.setVisible(false);
        mailError.setVisible(false);
        typereclamationError.setVisible(false);
        dateError.setVisible(false); // Hide the date error initially

        boolean isValid = true;

        // Validation checks
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        // Validate email format
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (mail.getText().trim().isEmpty() || !mail.getText().trim().matches(emailPattern)) {
            mailError.setText("Invalid email format");
            mailError.setVisible(true);
            isValid = false;
        }

        // Validate typereclamation
        if (typereclamation.getValue() == null) {
            typereclamationError.setText("Status is required");
            typereclamationError.setVisible(true);
            isValid = false;
        }

        // Validate date
        if (datePicker.getValue() == null) {
            dateError.setText("Date is required");
            dateError.setVisible(true);
            isValid = false;
        }

        // If any validation fails, stop further execution
        if (!isValid) {
            return;
        }

        // Update the selected reclamation
        if (selectedReclamation != null) {
            selectedReclamation.setDescription(description.getText());
            selectedReclamation.setMail(mail.getText());
            selectedReclamation.setTypereclamation(typereclamation.getValue());

            // Set the selected date from the DatePicker
            selectedReclamation.setDate(Date.valueOf(datePicker.getValue())); // Convert LocalDate to java.sql.Date

            ITuteurService.ReclamationService reclamationService = new ITuteurService.ReclamationService();
            reclamationService.modifier(selectedReclamation); // Call the service to update the reclamation

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Reclamation updated successfully");
            alert.showAndWait();

            // Redirect to the reclamation list page
            GoToAfficherreclamation(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No reclamation selected");
            alert.showAndWait();
        }
    }
}
