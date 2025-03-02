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
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import reclamations.entities.Reponse; // Changed from Reclamation to Reponse
import reclamations.services.ReponseService; // Changed from ReclamationService to ReponseService

public class updatereponseController {

    @FXML
    private TextArea description;

    @FXML
    private DatePicker datePicker; // Changed from mail to datePicker

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label descriptionError;

    @FXML
    private Label dateError; // Changed from mailError to dateError

    @FXML
    private Label statutError;

    private Reponse selectedReponse; // Changed from selectedReclamation to selectedReponse

    public void setReponseData(Reponse reponse) { // Changed from setReclamationData to setReponseData
        this.selectedReponse = reponse; // Changed from selectedReclamation to selectedReponse
        description.setText(reponse.getDescription());
        datePicker.setValue(reponse.getDate().toLocalDate()); // Changed from mail to datePicker
        statut.setValue(reponse.getStatut());
    }

    @FXML
    public void GoToAfficherreponse(ActionEvent actionEvent) { // Changed from GoToAfficherreclamation to GoToAfficherreponse
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreponse.fxml")); // Changed from afficherreclamtions.fxml to afficherreponse.fxml
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the response page"); // Changed from reclamation to response
            alert.showAndWait();
        }
    }

    @FXML
    public void Updatereponse(ActionEvent event) { // Changed from Updatereclamation to Updatereponse
        descriptionError.setVisible(false);
        dateError.setVisible(false); // Changed from mailError to dateError
        statutError.setVisible(false);

        boolean isValid = true;

        // Validation checks
        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        if (datePicker.getValue() == null) { // Changed from mail to datePicker
            dateError.setText("Date is required"); // Changed from mailError to dateError
            dateError.setVisible(true);
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

        if (selectedReponse != null) { // Changed from selectedReclamation to selectedReponse
            selectedReponse.setDescription(description.getText());
            selectedReponse.setDate(Date.valueOf(datePicker.getValue())); // Changed from mail to datePicker
            selectedReponse.setStatut(statut.getValue());

            ReponseService reponseService = new ReponseService(); // Changed from ReclamationService to ReponseService
            reponseService.modifier(selectedReponse); // Changed from selectedReclamation to selectedReponse

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Response updated successfully"); // Changed from Reclamation to Response
            alert.showAndWait();

            GoToAfficherreponse(event); // Changed from GoToAfficherreclamation to GoToAfficherreponse
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No response selected"); // Changed from reclamation to response
            alert.showAndWait();
        }
    }
}