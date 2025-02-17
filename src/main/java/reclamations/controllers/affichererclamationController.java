package reclamations.controllers;
import reclamations.controllers.updaterclamationController;  // Correct import

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;

import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import reclamations.services.ReclamationService;
import reclamations.entities.Reclamation;

public class affichererclamationController {@FXML
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
    private TableColumn<Reclamation, String> columnrole;

    @FXML
    public void initialize() {
        ReclamationService reclamationService = new ReclamationService();

        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.getall());
        tableViewReclamations.setItems(reclamations);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colummail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        columncontenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnstatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        columnrole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    @FXML
    public void GoToAddreclamation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterReclamation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load add reclamation page");
            alert.showAndWait();
        }
    }

    @FXML
    public void Deletererclamation(ActionEvent actionEvent) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            int reclamationId = selectedReclamation.getId();

            ReclamationService reclamationService = new ReclamationService();
            reclamationService.supprimer(reclamationId);

            tableViewReclamations.getItems().remove(selectedReclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Reclamation deleted successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No reclamation selected");
            alert.setContentText("Please select a reclamation to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    public void GoToUpdatereclamation(ActionEvent actionEvent) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateReclamation.fxml"));
                Parent root = loader.load();

                updaterclamationController controller = loader.getController();
                controller.setReclamationData(selectedReclamation);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load update reclamation page");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Reclamation Selected");
            alert.setHeaderText("Please select a reclamation to update");
            alert.showAndWait();
        }
    }
}

