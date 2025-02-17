package reclamations.controllers;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;  // Import java.sql.Date

// Import missing classes
import reclamations.entities.Reclamation;
import reclamations.services.ReclamationService;

public class addreclamationController {

    @FXML
    private TextField mail;

    @FXML
    private TextArea description;

    @FXML
    private TextField date;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label mailError;

    @FXML
    private Label descriptionError;

    @FXML
    private Label dateError;

    @FXML
    private Label statutError;

    @FXML
    public void initialize() {
        statut.setItems(FXCollections.observableArrayList("En attente", "Traitée", "Rejetée"));
    }

    @FXML
    void addReclamation(ActionEvent event) {
        boolean isValid = true;

        mailError.setVisible(false);
        descriptionError.setVisible(false);
        dateError.setVisible(false);
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

        // Validate date format
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        if (date.getText().trim().isEmpty() || !date.getText().trim().matches(datePattern)) {
            dateError.setText("Date must be in format YYYY-MM-DD");
            dateError.setVisible(true);
            isValid = false;
        }

        // Validate statut
        if (statut.getValue() == null) {
            statutError.setText("Statut is required");
            statutError.setVisible(true);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Convert String date to java.sql.Date
        Date sqlDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(date.getText());
            sqlDate = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            dateError.setText("Invalid date format, must be YYYY-MM-DD");
            dateError.setVisible(true);
            return;
        }

        // Create a new Reclamation object
        Reclamation reclamation = new Reclamation();
        reclamation.setMail(mail.getText());
        reclamation.setDescription(description.getText());
        reclamation.setDate(sqlDate);  // Set the converted date
        reclamation.setStatut(statut.getValue());

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

    @FXML
    public void GoToAfficherReclamations(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReclamations.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the reclamations page");
            alert.setContentText("An error occurred while trying to navigate back to the reclamation list.");
            alert.showAndWait();
        }
    }
}