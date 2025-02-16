package esprit.tn.controllers;

import esprit.tn.entities.Dons;
import esprit.tn.entities.donateur;
import esprit.tn.services.DonateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class AfficherDonateurController {

    @FXML
    private TableView<donateur> TableDonateur;
    private TableView<Dons> TableDons;


    @FXML
    private TableColumn<donateur, String> adresse;

    @FXML
    private TableColumn<donateur, String> email;

    @FXML
    private TableColumn<donateur, String> nom;

    @FXML
    private TableColumn<donateur, String> prenom;

    @FXML
    private TableColumn<donateur, Integer> telephone;


    @FXML
    void initialize() {
        DonateurService ds = new DonateurService();

        ObservableList<donateur> observableList = FXCollections.observableList(ds.getall());
        TableDonateur.setItems(observableList);
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

    }
    @FXML
    void AjouterDt(ActionEvent event)  {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterDonateur.fxml")));
            TableDonateur.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ModifierDt(javafx.event.ActionEvent actionEvent) {
        // Vérifier si un donnateur est sélectionné dans la TableDonateur
        donateur donateurSelectionne = TableDonateur.getSelectionModel().getSelectedItem();

        if (donateurSelectionne == null) {
            // Si aucun donateur n'est sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un donateur à modifier !");
            alert.showAndWait();
            return;
        }else {

            try {
                // Si un donateur est sélectionné, on passe ses informations à la page de modification
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDonateur.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de la page ModifierVisiteur
                ModifierDonateurController controller = loader.getController();
                controller.initData(donateurSelectionne);  // Passe le donateur sélectionné au contrôleur de la page de modification

                // Changer la scène pour afficher la page de modification
                Stage stage = (Stage) TableDonateur.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @FXML
    void supprimerDt(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée
        donateur donateurSelectionne = TableDonateur.getSelectionModel().getSelectedItem();

        if (donateurSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un donateur à supprimer !");
            alert.showAndWait();
            return;
        }

        // Confirmation de suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du donateur");
        confirmation.setContentText("Voulez-vous vraiment supprimer " + donateurSelectionne.getNom() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer de la base de données
            DonateurService ds = new DonateurService();
            ds.supprimer(donateurSelectionne.getId());

            // Supprimer de la TableView
            TableDonateur.getItems().remove(donateurSelectionne);

            // Afficher une alerte de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le donateur a été supprimé avec succès !");
            success.showAndWait();
        }
    }

    @FXML
    void faireUnDon(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherDons.fxml")));
            TableDonateur.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






}
