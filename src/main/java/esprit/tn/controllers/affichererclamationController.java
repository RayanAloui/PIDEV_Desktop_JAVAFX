package esprit.tn.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import esprit.tn.entities.Reclamation;
import esprit.tn.services.ReclamationService;

import java.io.IOException;

public class affichererclamationController {

    @FXML
    private TableView<Reclamation> tableViewReclamations;

    @FXML
    private TableColumn<Reclamation, Integer> columnId;

    @FXML
    private TableColumn<Reclamation, String> colummail;

    @FXML
    private TableColumn<Reclamation, String> columncontenu;

    @FXML
    private TableColumn<Reclamation, String> columndate;

    @FXML
    private TableColumn<Reclamation, String> columnstatut;

    @FXML
    public void initialize() {
        // Initialize TableColumn bindings
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colummail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        columncontenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnstatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Load data into the TableView
        loadReclamations();
    }

    private void loadReclamations() {
        ReclamationService reclamationService = new ReclamationService();
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.getall());
        tableViewReclamations.setItems(reclamations);
    }

    @FXML
    void GoToAddreclamation(ActionEvent event) {
        try {
            // Load the FXML file for adding a reclamation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreclamations.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            // Show an error alert if the page fails to load
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load add reclamation page");
            alert.setContentText("An error occurred while loading the page: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void Deletererclamation(ActionEvent event) {
        // Get the selected reclamation from the TableView
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            // Delete the reclamation using the service
            ReclamationService reclamationService = new ReclamationService();
            reclamationService.supprimer(selectedReclamation.getId());

            // Refresh the TableView
            loadReclamations();

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Reclamation deleted successfully");
            alert.showAndWait();
        } else {
            // Show a warning if no reclamation is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No reclamation selected");
            alert.setContentText("Please select a reclamation to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void GoToUpdatereclamation(ActionEvent event) {
        // Get the selected reclamation from the TableView
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            try {
                // Load the FXML file for updating a reclamation
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatereclamation.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected reclamation data
                updaterclamationController controller = loader.getController();
                controller.setReclamationData(selectedReclamation);

                // Get the current stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set the new scene
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

                // Show an error alert if the page fails to load
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load update reclamation page");
                alert.setContentText("An error occurred while loading the page: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show a warning if no reclamation is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Reclamation Selected");
            alert.setHeaderText("Please select a reclamation to update");
            alert.showAndWait();
        }
    }
}