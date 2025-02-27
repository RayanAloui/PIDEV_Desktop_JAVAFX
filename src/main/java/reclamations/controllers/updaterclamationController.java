package reclamations.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;  // Added import for TextField
import javafx.scene.Parent;     // Added import for Parent
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import reclamations.entities.Reclamation; // Added import for Reclamation class
import reclamations.services.ReclamationService; // Added import for ReclamationService

public class updaterclamationController {

    @FXML
    private TextArea description;



    @FXML
    private TextField mail;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label descriptionError;



    @FXML
    private Label mailError;

    @FXML
    private Label statutError;

    private Reclamation selectedReclamation;

    public void setReclamationData(Reclamation reclamation) {
        this.selectedReclamation = reclamation;
        description.setText(reclamation.getDescription());
        mail.setText(reclamation.getMail());

        statut.setValue(reclamation.getStatut());
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
        statutError.setVisible(false);

        boolean isValid = true;

        // Validation checks
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (mail.getText().trim().isEmpty() || !mail.getText().trim().matches(emailPattern)) {
            mailError.setText("Invalid email format");
            mailError.setVisible(true);
            isValid = false;
        }
        if (statut.getValue() == null) {
            statutError.setText("Status is required");
            statutError.setVisible(true);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        if (selectedReclamation != null) {
            selectedReclamation.setDescription(description.getText());
            selectedReclamation.setMail(mail.getText());

            selectedReclamation.setStatut(statut.getValue());

            ReclamationService reclamationService = new ReclamationService();
            reclamationService.modifier(selectedReclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Reclamation updated successfully");
            alert.showAndWait();

            GoToAfficherreclamation(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No reclamation selected");
            alert.showAndWait();
        }
    }
}