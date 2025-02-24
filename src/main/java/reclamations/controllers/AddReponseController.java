package reclamations.controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import reclamations.entities.Reclamation;
import reclamations.entities.Reponse;
import reclamations.services.ReclamationService;
import reclamations.services.ReponseService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.LocalDate;

public class AddReponseController {

    @FXML
    private TextArea description;

    @FXML
    private Label descriptionError;


    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label statutError;

    @FXML
    public void initialize() {
        // Initialize error labels to be invisible by default
        descriptionError.setVisible(false);

        statutError.setVisible(false);

        // Set default text for error labels (optional)
        descriptionError.setText("Description is required");

        statutError.setText("Statut is required");

        // Clear any existing text in input fields (optional)
        description.clear();

        statut.getSelectionModel().clearSelection();
    }

    @FXML
    void GoToAfficherReponses(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    void addReponse(ActionEvent event) {

        boolean isValid = true;

        // Reset error messages

        descriptionError.setVisible(false);
        statutError.setVisible(false);




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

        // Create a new reponse object
        Reponse reponse = new Reponse();

        reponse.setDescription(description.getText());
        reponse.setStatut(statut.getValue());
        reponse.setDate(Date.valueOf(LocalDate.now()));

        // Add reponse to the database
        ReponseService reponseService = new ReponseService();
        reponseService.ajouter(reponse);

        // Show success alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reponse Added");
        alert.setHeaderText("reponse added successfully");
        alert.showAndWait();

        // Redirect to the reponse list page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load the reponse list page");
            errorAlert.showAndWait();
        }
    }
}