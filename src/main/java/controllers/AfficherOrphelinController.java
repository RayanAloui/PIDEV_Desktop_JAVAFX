package controllers;

import entities.Tuteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Orphelin;
import javafx.stage.Stage;
import services.ServiceOrphelin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherOrphelinController {

    @FXML
    private TableView<Orphelin> tableOrphelins;
    @FXML
    private TableColumn<Orphelin, String> colNom;
    @FXML
    private TableColumn<Orphelin, String> colPrenom;
    @FXML
    private TableColumn<Orphelin, String> colDateNaissance;
    @FXML
    private TableColumn<Orphelin, String> colSexe;
    @FXML
    private TableColumn<Orphelin, String> colSituation;
    @FXML
    private TableColumn<Orphelin, String> colTuteur;

    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin();

    @FXML
    public void initialize() {
        try {
            loadOrphelins();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadOrphelins() throws SQLException {
        List<Orphelin> orphelins = serviceOrphelin.getAllOrphelins();
        ObservableList<Orphelin> observableList = FXCollections.observableArrayList(orphelins);

        colNom.setCellValueFactory(new PropertyValueFactory<>("nomO"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomO"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colSexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        colSituation.setCellValueFactory(new PropertyValueFactory<>("situationScolaire"));
        colTuteur.setCellValueFactory(new PropertyValueFactory<>("idTuteur"));

        tableOrphelins.setItems(observableList);
    }

    @FXML
    void ajouterOrphelin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOrphelin.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Orphelin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void afficherTut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTuteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Tuteurs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateO(ActionEvent event) {
        Orphelin selectedOrphelin = tableOrphelins.getSelectionModel().getSelectedItem();

        if (selectedOrphelin != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrphelin.fxml"));
                Parent root = loader.load();

                // Passer les données du tuteur sélectionné au contrôleur de mise à jour
                UpdateOrphelinController updateController = loader.getController();
                updateController.initData(selectedOrphelin);


                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Mettre à jour un Tuteur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ Aucun tuteur sélectionné !");
        }
    }

    public void deleteO(ActionEvent actionEvent) {
        // Vérifier si un orphelin est sélectionné
        Orphelin selectedOrphelin = tableOrphelins.getSelectionModel().getSelectedItem();

        if (selectedOrphelin == null) {
            // Afficher une alerte si aucun tuteur n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un orphelin à supprimer.");
            alert.showAndWait();
            return;
        }

        // Boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet orphelin ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer le tuteur de la base de données
                serviceOrphelin.delete(selectedOrphelin.getIdO());

                // Supprimer le tuteur de la TableView
                tableOrphelins.getItems().remove(selectedOrphelin);

                // Afficher un message de succès
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Suppression réussie");
                success.setHeaderText(null);
                success.setContentText("L'orphelin a été supprimé avec succès !");
                success.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setHeaderText(null);
                error.setContentText("Erreur lors de la suppression d'orphelin .");
                error.showAndWait();
            }
        }
    }
}

