package esprit.tn.controllers;
import esprit.tn.entities.Dons;
import esprit.tn.services.DonsService;
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
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class AfficherDonsController {

    @FXML
    private TableView<Dons> TableDons;

    @FXML
    private TableColumn<Dons, Date> dateDon;

    @FXML
    private TableColumn<Dons, String> description;

    @FXML
    private TableColumn<Dons, Double> montant;

    @FXML
    private TableColumn<Dons, String> statut;

    @FXML
    private TableColumn<Dons, String> typeDon;

    @FXML
    private TableColumn<Dons, Integer> donateurId; // Ajout de la colonne donateurId

    @FXML
    void initialize() {
        try {
            DonsService ds = new DonsService();
            ObservableList<Dons> observableList = FXCollections.observableList(ds.getall());
            TableDons.setItems(observableList);

            // Correction des noms des propriétés
            dateDon.setCellValueFactory(new PropertyValueFactory<>("dateDon"));
            System.out.println("dateDon = " + dateDon);
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
            statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            typeDon.setCellValueFactory(new PropertyValueFactory<>("typeDon"));
            System.out.println("typeDon = " + typeDon);
            donateurId.setCellValueFactory(new PropertyValueFactory<>("donateurId")); // Ajout de la colonne donateurId
        } catch (Exception e) {
            e.printStackTrace();
            // Afficher une alerte à l'utilisateur en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement des dons");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void AjouterD(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AjouterDon.fxml")));
            TableDons.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ModifierD(ActionEvent event) {
        // Vérifier si un donnateur est sélectionné dans la TableDonateur
        Dons donSelectionne = TableDons.getSelectionModel().getSelectedItem();

        if (donSelectionne == null) {
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDon.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de la page ModifierVisiteur
                ModifierDonController controller = loader.getController();
                controller.initDataDon(donSelectionne);  // Passe le donateur sélectionné au contrôleur de la page de modification

                // Changer la scène pour afficher la page de modification
                Stage stage = (Stage) TableDons.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void supprimerD(ActionEvent event) {
        // Implémentez cette fonction
        // Vérifier si un donateur est sélectionné dans la TableDonateur
        Dons donSelectionne = TableDons.getSelectionModel().getSelectedItem();

        if (donSelectionne == null) {
            // Si aucun donateur n'est sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un donateur à supprimer!");
            alert.showAndWait();
            return;
        }

        // Demander confirmation avant la suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du don");
        confirmation.setContentText("Voulez-vous vraiment supprimer ce  " + donSelectionne.getTypeDon() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer de la base de données
            DonsService ds = new DonsService();
            ds.supprimer(donSelectionne.getId());

            // Supprimer de la TableView
            TableDons.getItems().remove(donSelectionne);

            // Afficher une alerte de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le donateur a été supprimé avec succès !");
            success.showAndWait();
        }
    }


}

