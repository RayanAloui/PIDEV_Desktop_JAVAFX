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
import reclamations.entities.Reponse;
import reclamations.services.ReponseService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class AddReponseController {

    @FXML
    private TextArea description;

    @FXML
    private TextField date;

    @FXML
    private ChoiceBox<String> statut;

    @FXML
    private Label descriptionError;

    @FXML
    private Label dateError;

    @FXML
    private Label statutError;

    @FXML
    public void initialize() {
        statut.setItems(FXCollections.observableArrayList("Pending", "Resolved", "Closed"));
    }

    @FXML
    void addReponse(ActionEvent event) {
        boolean isValid = true;

        descriptionError.setVisible(false);
        dateError.setVisible(false);
        statutError.setVisible(false);

        if (description.getText().trim().isEmpty()) {
            descriptionError.setText("Description is required");
            descriptionError.setVisible(true);
            isValid = false;
        }

        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        Date sqlDate = null;

        if (date.getText().trim().isEmpty() || !date.getText().trim().matches(datePattern)) {
            dateError.setText("Date must be in format YYYY-MM-DD");
            dateError.setVisible(true);
            isValid = false;
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                java.util.Date parsedDate = dateFormat.parse(date.getText().trim());
                sqlDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                dateError.setText("Invalid date. Please use format YYYY-MM-DD");
                dateError.setVisible(true);
                isValid = false;
            }
        }

        if (statut.getValue() == null) {
            statutError.setText("Statut is required");
            statutError.setVisible(true);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        Reponse reponse = new Reponse(description.getText(), sqlDate, statut.getValue());

        ReponseService reponseService = new ReponseService();
        reponseService.ajouter(reponse);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Response Added");
        alert.setHeaderText("Response added successfully");
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReponses.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to load the response list page");
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void goToAfficherReponses(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReponses.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the responses page");
            alert.setContentText("An error occurred while trying to navigate back to the response list.");
            alert.showAndWait();
        }
    }
}