package controllers;

import entities.Tuteur;
import services.ServiceTuteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class AfficherTuteurController {

    @FXML
    private TableView<Tuteur> tableTuteurs;

    @FXML
    private TableColumn<Tuteur, String> colId;

    @FXML
    private TableColumn<Tuteur, String> colCin;

    @FXML
    private TableColumn<Tuteur, String> colNom;

    @FXML
    private TableColumn<Tuteur, String> colPrenom;

    @FXML
    private TableColumn<Tuteur, String> colTelephone;

    @FXML
    private TableColumn<Tuteur, String> colAdresse;

    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    @FXML
    public void initialize() {
        try {
            loadTuteurs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTuteurs() throws SQLException {
        List<Tuteur> tuteurs = serviceTuteur.getAllTuteurs();
        ObservableList<Tuteur> observableList = FXCollections.observableArrayList(tuteurs);

        // Associer les colonnes aux attributs de l'entité Tuteur
        colId.setCellValueFactory(new PropertyValueFactory<>("idT"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cinT"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomT"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomT"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephoneT"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresseT"));

        // Remplir la table avec les données
        tableTuteurs.setItems(observableList);
    }



    @FXML
    void ajouterTuteur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTuteur.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Tuteur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void updateT(ActionEvent event) {
        Tuteur selectedTuteur = tableTuteurs.getSelectionModel().getSelectedItem();

        if (selectedTuteur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateTuteur.fxml"));
                Parent root = loader.load();

                // Passer les données du tuteur sélectionné au contrôleur de mise à jour
                UpdateTuteurController updateController = loader.getController();
                updateController.initData(selectedTuteur);


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

    @FXML
    void deleteT(ActionEvent event) {
        // Vérifier si un tuteur est sélectionné
        Tuteur selectedTuteur = tableTuteurs.getSelectionModel().getSelectedItem();

        if (selectedTuteur == null) {
            // Afficher une alerte si aucun tuteur n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un tuteur à supprimer.");
            alert.showAndWait();
            return;
        }

        // Boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce tuteur ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer le tuteur de la base de données
                serviceTuteur.delete(selectedTuteur.getIdT());

                // Supprimer le tuteur de la TableView
                tableTuteurs.getItems().remove(selectedTuteur);

                // Afficher un message de succès
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Suppression réussie");
                success.setHeaderText(null);
                success.setContentText("Le tuteur a été supprimé avec succès !");
                success.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setHeaderText(null);
                error.setContentText("Erreur lors de la suppression du tuteur.");
                error.showAndWait();
            }
        }
    }





}

