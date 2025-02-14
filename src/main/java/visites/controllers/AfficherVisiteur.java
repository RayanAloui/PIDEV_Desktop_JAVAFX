package visites.controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import visites.entities.visiteurs;
import visites.services.VisiteursService;

import java.io.IOException;
import java.util.Optional;

public class AfficherVisiteur {

    @FXML
    private TableView<visiteurs> TableView;

    @FXML
    private TableColumn<visiteurs, String> adresse;

    @FXML
    private TableColumn<visiteurs, String> email;

    @FXML
    private TableColumn<visiteurs, String> nom;

    @FXML
    private TableColumn<visiteurs, String> prenom;

    @FXML
    private TableColumn<visiteurs, Integer> tel;

    @FXML
    void initialize(){
        VisiteursService vs=new VisiteursService();
        ObservableList<visiteurs> observableList= FXCollections.observableList(vs.getall());
        TableView.setItems(observableList);
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));


    }

    @FXML
    void ajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/AjouterVisiteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) TableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void modifier(ActionEvent event) {
        // Vérifier si un visiteur est sélectionné dans la TableView
        visiteurs visiteurSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteurSelectionne == null) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/ModifierVisiteur.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierVisiteur
            ModifierVisiteur controller = loader.getController();
            controller.setVisiteur(visiteurSelectionne);  // Passe le visiteur sélectionné au contrôleur de la page de modification

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
        visiteurs visiteurSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteurSelectionne == null) {
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
        confirmation.setContentText("Voulez-vous vraiment supprimer " + visiteurSelectionne.getNom() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer de la base de données
            VisiteursService vs = new VisiteursService();
            vs.supprimer(visiteurSelectionne.getId());

            // Supprimer de la TableView
            TableView.getItems().remove(visiteurSelectionne);

            // Afficher une alerte de succès
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le visiteur a été supprimé avec succès !");
            success.showAndWait();
        }
    }


}
