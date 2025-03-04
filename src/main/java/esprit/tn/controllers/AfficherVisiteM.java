package esprit.tn.controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import esprit.tn.entities.visites;
import esprit.tn.services.VisitesService;

import java.io.IOException;
import java.util.Optional;

public class AfficherVisiteM {

    @FXML
    private TableView<visites> TableView;

    @FXML
    private TableColumn<visites, String> date;

    @FXML
    private TableColumn<visites, String> heure;

    @FXML
    private TableColumn<visites, String> motif;

    @FXML
    private TableColumn<visites, String> statut;

    @FXML
    void initialize() {
        VisitesService vs = new VisitesService();
        ObservableList<visites> observableList = FXCollections.observableList(vs.getall());
        TableView.setItems(observableList);

        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        heure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        motif.setCellValueFactory(new PropertyValueFactory<>("motif"));
        statut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Permettre le tri automatique des colonnes
        date.setSortable(true);
        heure.setSortable(true);

        // Ajouter un écouteur d'événements pour trier par date
        date.setSortType(TableColumn.SortType.ASCENDING);
        date.setOnEditStart(event -> toggleSort(date));

        // Ajouter un écouteur d'événements pour trier par heure
        heure.setSortType(TableColumn.SortType.ASCENDING);
        heure.setOnEditStart(event -> toggleSort(heure));
    }

    private void toggleSort(TableColumn<visites, ?> column) {
        if (column.getSortType() == TableColumn.SortType.ASCENDING) {
            column.setSortType(TableColumn.SortType.DESCENDING);
        } else {
            column.setSortType(TableColumn.SortType.ASCENDING);
        }
        TableView.getSortOrder().clear();
        TableView.getSortOrder().add(column);
    }


    @FXML
    public void retour(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


            stage.setScene(new Scene(root));


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the users page");
            alert.setContentText("An error occurred while trying to navigate back to the user list.");
            alert.showAndWait();
        }
    }


    @FXML
    void modifier(ActionEvent event) {
        // Vérifier si un visiteur est sélectionné dans la TableView
        visites visiteSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteSelectionne == null) {
            // Si aucun visiteur n'est sélectionné, afficher une alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un visiteur à modifier !");
            alert.showAndWait();
            return;
        }

        try {
            // Si un visiteur est sélectionné, on passe ses informations à la page de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/ModifierVisiteM.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierVisiteur
            ModifierVisiteM controller = loader.getController();
            controller.setVisite(visiteSelectionne);  // Passe le visiteur sélectionné au contrôleur de la page de modification

            // Changer la scène pour afficher la page de modification
            Stage stage = (Stage) TableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void supprimer(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée
        visites visiteSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un visiteur à supprimer !");
            alert.showAndWait();
            return;
        }

        // Confirmation de suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du visiteur");
        confirmation.setContentText("Voulez-vous vraiment supprimer le RDV a la date: " + visiteSelectionne.getDate() +" et a l'heure:"+ visiteSelectionne.getHeure() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer de la base de données
            VisitesService vs = new VisitesService();
            vs.supprimer(visiteSelectionne.getId());

            // Supprimer de la TableView
            TableView.getItems().remove(visiteSelectionne);

            // Afficher une alerte de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le visiteur a été supprimé avec succès !");
            success.showAndWait();
        }
    }

    @FXML
    void visiteurs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/AfficherVisiteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) TableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
