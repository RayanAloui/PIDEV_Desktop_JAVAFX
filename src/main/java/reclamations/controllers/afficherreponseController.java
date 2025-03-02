package reclamations.controllers;

import reclamations.controllers.updatereponseController;  // Correct import

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import reclamations.services.ReponseService;
import reclamations.entities.Reponse;

public class afficherreponseController {

    @FXML
    private TableView<Reponse> tableViewReponses;

    @FXML
    private TableColumn<Reponse, Integer> columnId;

    @FXML
    private TableColumn<Reponse, String> columndescription;

    @FXML
    private TableColumn<Reponse, String> columndate;

    @FXML
    private TableColumn<Reponse, String> columnstatut;

    @FXML
    public void initialize() {
        // Removed the column for idReclamation
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columndescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnstatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreponse.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load add response page");
            alert.setContentText("An error occurred while loading the page: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void Deletereponse(ActionEvent event) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse != null) {
            ReponseService reponseService = new ReponseService();
            reponseService.supprimer(selectedReponse.getId());
            loadReponses();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Response deleted successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No response selected");
            alert.setContentText("Please select a response to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void GoToUpdatereponse(ActionEvent event) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();

        if (selectedReponse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatereponse.fxml"));
                Parent root = loader.load();
                updatereponseController controller = loader.getController();
                controller.setReponseData(selectedReponse);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load update response page");
                alert.setContentText("An error occurred while loading the page: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Response Selected");
            alert.setHeaderText("Please select a response to update");
            alert.showAndWait();
        }
    }
}
