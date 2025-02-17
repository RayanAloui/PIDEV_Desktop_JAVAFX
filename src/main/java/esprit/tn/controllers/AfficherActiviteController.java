package esprit.tn.controllers;

import esprit.tn.entities.Activite;
import esprit.tn.services.ActiviteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherActiviteController implements Initializable {

    @FXML
    private TableView<Activite> tableViewActivites;

    @FXML
    private TableColumn<Activite, Integer> columnId;

    @FXML
    private TableColumn<Activite, String> columnNom;

    @FXML
    private TableColumn<Activite, String> columnCategorie;

    @FXML
    private TableColumn<Activite, String> columnDuree;

    @FXML
    private TableColumn<Activite, String> columnNomTuteur;

    @FXML
    private TableColumn<Activite, String> columnDateActivite;

    @FXML
    private TableColumn<Activite, String> columnLieu;

    @FXML
    private TableColumn<Activite, String> columnDescription;

    @FXML
    private TableColumn<Activite, String> columnStatut;
    private static Activite selectedActivite;

    @FXML
    private ComboBox<String> comboBoxCategorie;

    private ActiviteService activiteService = new ActiviteService();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize TableView columns
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        columnDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        columnNomTuteur.setCellValueFactory(new PropertyValueFactory<>("nom_du_tuteur"));
        columnDateActivite.setCellValueFactory(new PropertyValueFactory<>("date_activite"));
        columnLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Load all activities into the TableView
        loadActivites();
    }

    private void loadActivites() {
        List<Activite> activites = activiteService.getall(null);
        ObservableList<Activite> observableList = FXCollections.observableArrayList(activites);
        tableViewActivites.setItems(observableList);
    }

    @FXML
    private void filterActivitesByCategorie() {
        String categorie = comboBoxCategorie.getValue();
        List<Activite> activites = activiteService.getall(null); // Replace with filtered list if needed
        ObservableList<Activite> observableList = FXCollections.observableArrayList(activites);
        tableViewActivites.setItems(observableList);
    }

    @FXML
    private void GoToAddActivite() {
        try {
            // Load the AddActivite.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterActivite.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) tableViewActivites.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AddActivite.fxml");
        }
    }

    @FXML
    private void DeleteActivite() {

        Activite selectedActivite = tableViewActivites.getSelectionModel().getSelectedItem();

        if (selectedActivite != null) {

            activiteService.supprimer(selectedActivite.getId());


            tableViewActivites.getItems().remove(selectedActivite);


            System.out.println("Activité supprimée avec succès !");
        } else {
            // Show an error message if no activity is selected
            System.out.println("Veuillez sélectionner une activité à supprimer.");
        }
    }

    public static Activite getSelectedActivite() {
        return selectedActivite;
    }

    public static void setSelectedActivite(Activite activite) {
        selectedActivite = activite;
    }
    @FXML
    private void GoToUpdateActivite() {
        // Get the selected activity from the TableView
        selectedActivite = tableViewActivites.getSelectionModel().getSelectedItem();

        if (selectedActivite != null) {
            try {
                // Load the UpdateActivite.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateActivite.fxml"));
                Parent root = loader.load();

                // Get the current stage (window)
                Stage stage = (Stage) tableViewActivites.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading UpdateActivite.fxml");
            }
        } else {
            System.out.println("Veuillez sélectionner une activité à modifier.");
        }
    }
}