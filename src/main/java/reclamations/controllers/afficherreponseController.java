package reclamations.controllers;
import reclamations.entities.Reponse;

import reclamations.services.ReponseService;

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
import java.io.IOException;
import reclamations.controllers.updatereponseController;  // Correct import


public class afficherreponseController {

    @FXML
    private TableView<Reponse> tableViewReponses;

    @FXML
    private TableColumn<Reponse, Integer> columnId;



    @FXML
    private TableColumn<Reponse, String> columncontenu;

    @FXML
    private TableColumn<Reponse, String> columndate;

    @FXML
    private TableColumn<Reponse, String> columnstatut;

    @FXML
    public void initialize() {
        // Initialize TableColumn bindings
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        columncontenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnstatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Load data into the TableView
        loadReponses();
    }

    private void loadReponses() {
        ReponseService reponseService = new ReponseService();
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(reponseService.getall());
        tableViewReponses.setItems(reponses);
    }

    @FXML
    void GoToAddreponse(ActionEvent event) {
        try {
            // Load the FXML file for adding a reponse
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreponse.fxml"));
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
            alert.setHeaderText("Failed to load add reponse page");
            alert.setContentText("An error occurred while loading the page: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void Deletereponse(ActionEvent event) {
        // Get the selected reponse from the TableView
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse != null) {
            // Delete the reponse using the service
            ReponseService reponseService = new ReponseService();
            reponseService.supprimer(selectedReponse.getId());

            // Refresh the TableView
            loadReponses();

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Reponse deleted successfully");
            alert.showAndWait();
        } else {
            // Show a warning if no reponse is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No reponse selected");
            alert.setContentText("Please select a reponse to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void GoToUpdatereponse(ActionEvent event) {
        // Get the selected reponse from the TableView
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse != null) {
            try {
                // Load the FXML file for updating a reponse
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatereponse.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected reponse data
                updatereponseController controller = loader.getController();
                controller.setReponseData(selectedReponse);

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
                alert.setHeaderText("Failed to load update reponse page");
                alert.setContentText("An error occurred while loading the page: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show a warning if no reponse is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Reponse Selected");
            alert.setHeaderText("Please select a reponse to update");
            alert.showAndWait();
        }
    }
}