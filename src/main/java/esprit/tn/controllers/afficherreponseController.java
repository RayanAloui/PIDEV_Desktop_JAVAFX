package esprit.tn.controllers;
import esprit.tn.entities.Reponse;
import esprit.tn.services.ReponseService;
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


public class afficherreponseController {

    @FXML
    private TableView<Reponse> tableViewReponses;

    @FXML
    private TableColumn<Reponse, Integer> columnId;
    @FXML
    private TableColumn<Reponse, String> columnDescription;
    @FXML
    private TableColumn<Reponse, String> columnDate;
    @FXML
    private TableColumn<Reponse, String> columnStatut;

    @FXML
    public void initialize() {
        ReponseService reponseService = new ReponseService();
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(reponseService.getall());
        tableViewReponses.setItems(reponses);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    @FXML
    public void GoToAddReponse(ActionEvent actionEvent) {
        navigateTo(actionEvent, "/ajouterReponse.fxml", "Failed to load add response page");
    }

    @FXML
    public void DeleteReponse(ActionEvent actionEvent) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();
        if (selectedReponse != null) {
            ReponseService reponseService = new ReponseService();
            reponseService.supprimer(selectedReponse.getId());
            tableViewReponses.getItems().remove(selectedReponse);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Response deleted successfully");
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a response to delete.");
        }
    }

    @FXML
    public void GoToUpdateReponse(ActionEvent actionEvent) {
        Reponse selectedReponse = tableViewReponses.getSelectionModel().getSelectedItem();
        if (selectedReponse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateReponse.fxml"));
                Parent root = loader.load();
                updatereponseController controller = loader.getController();  // Corrected class name
                controller.setReponseData(selectedReponse);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load update response page");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a response to update");
        }
    }

    private void navigateTo(ActionEvent actionEvent, String fxmlPath, String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}