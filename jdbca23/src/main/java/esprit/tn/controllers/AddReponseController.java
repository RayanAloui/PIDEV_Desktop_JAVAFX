package esprit.tn.controllers;

import esprit.tn.services.ITuteurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import esprit.tn.entities.Reponse;

import esprit.tn.services.EmailService;

import java.io.IOException;
import java.sql.Date;

public class AddReponseController {

    @FXML
    private Label descriptionError; // Error label for description
    @FXML
    private Label dateError;       // Error label for date
    @FXML
    private Label statutError;     // Error label for status

    private int reclamtionid;
    @FXML
    private DatePicker datePicker; // DatePicker for selecting the date

    @FXML
    private TextArea description;  // TextArea for entering the description

    @FXML
    private ChoiceBox<String> statut; // ChoiceBox for selecting the status

    private ITuteurService.ReponseService reponseService = new ITuteurService.ReponseService();

    public void getreclamationid(int id ){
        System.out.println(id);
        reclamtionid = id;
    }

    @FXML
    public void initialize() {
        // Initialize error labels to be invisible by default
        descriptionError.setVisible(false);
        dateError.setVisible(false);
        statutError.setVisible(false);

        // Set default text for error labels (optional)
        descriptionError.setText("Description is required");
        dateError.setText("Date is required");
        statutError.setText("Status is required");

        // Clear any existing text in input fields (optional)
        description.clear();
        datePicker.setValue(null);
        statut.getSelectionModel().clearSelection();
    }

    @FXML
    void GoToAfficherReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Could not load the response page: " + e.getMessage());
        }
    }

    @FXML
    void addReponse(ActionEvent event) {
        // Reset error messages
        descriptionError.setVisible(false);
        dateError.setVisible(false);
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

        // Validate status
        if (statut.getValue() == null) {
            statutError.setText("Status is required");
            statutError.setVisible(true);
            isValid = false;
        }

        // If validation fails, stop further execution
        if (!isValid) {
            return;
        }

        // Create a new Reponse object
        Reponse reponse = new Reponse();
        reponse.setDescription(description.getText());
        reponse.setDate(Date.valueOf(datePicker.getValue())); // Convert LocalDate to java.sql.Date
        reponse.setStatut(statut.getValue());
        reponse.setIndice(reclamtionid);

        // Add response to the database
        try {
            reponseService.ajouter(reponse);

            // Show success alert
            showInfo("Response added successfully!");

            // Send email notification
            sendResponseAddedEmail(reclamtionid);

            // Redirect to the response list page
            GoToAfficherReponse(event);
        } catch (Exception e) {
            showError("Failed to add response: " + e.getMessage());
        }
    }

    // Utility method to send an email after a response is added
    private void sendResponseAddedEmail(int reclamationId) {
        // Assuming you have a method to fetch the user email based on the reclamation ID
        String userEmail = getUserEmailByReclamationId(reclamationId);

        if (userEmail != null && !userEmail.isEmpty()) {
            // Set up the email details
            String subject = "New Response Added to Your Reclamation";
            String content = "Dear User,\n\n"
                    + "A new response has been added to your reclamation (ID: " + reclamationId + ").\n"
                    + "Response Description: " + description.getText() + "\n"
                    + "Response Status: " + statut.getValue() + "\n"
                    + "Response Date: " + datePicker.getValue() + "\n\n"
                    + "Thank you for using our service.\n\n"
                    + "Best regards,\nOrphenCare";

            // Send the email
            EmailService emailService = new EmailService();
            emailService.envoyerEmail(userEmail, subject, content);  // Send email to the user
        }
    }

    // Dummy method to fetch user email by reclamation ID (you should implement this logic)
    private String getUserEmailByReclamationId(int reclamationId) {
        // Fetch user email based on reclamation ID (implement database logic or fetch the email dynamically)
        // For now, return a placeholder email
        return "sarahbelhadej19@gmail.com";  // Placeholder email
    }

    // Utility method to show error alerts
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method to show info alerts
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
