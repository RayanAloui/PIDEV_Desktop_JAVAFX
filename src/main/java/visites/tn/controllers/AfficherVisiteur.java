package visites.tn.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import visites.tn.entities.visiteurs;
import visites.tn.services.VisiteursService;

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
    private TableColumn<visiteurs, Integer> cin; // ✅ Ajout de la colonne CIN

    @FXML
    private TextField recherche;

    @FXML
    void initialize() {
        VisiteursService vs = new VisiteursService();
        ObservableList<visiteurs> observableList = FXCollections.observableList(vs.getall());
        TableView.setItems(observableList);

        // Initialisation des colonnes ✅
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        cin.setCellValueFactory(new PropertyValueFactory<>("cin")); // ✅ Ajout du CIN

        // Recherche dynamique ✅
        recherche.textProperty().addListener((observable, oldValue, newValue) -> rechercher(null));
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
        visiteurs visiteurSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteurSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un visiteur à modifier !");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visiteur/ModifierVisiteur.fxml"));
            Parent root = loader.load();
            ModifierVisiteur controller = loader.getController();
            controller.setVisiteur(visiteurSelectionne);

            Stage stage = (Stage) TableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        visiteurs visiteurSelectionne = TableView.getSelectionModel().getSelectedItem();

        if (visiteurSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un visiteur à supprimer !");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du visiteur");
        confirmation.setContentText("Voulez-vous vraiment supprimer " + visiteurSelectionne.getNom() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            VisiteursService vs = new VisiteursService();
            vs.supprimer(visiteurSelectionne.getId());

            TableView.getItems().remove(visiteurSelectionne);

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Suppression réussie");
            success.setHeaderText(null);
            success.setContentText("Le visiteur a été supprimé avec succès !");
            success.showAndWait();
        }
    }

    @FXML
    void visites(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/AfficherVisite.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) TableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rechercher(ActionEvent event) {
        String searchText = recherche.getText().toLowerCase().trim();

        // Si le champ est vide, afficher tous les visiteurs
        if (searchText.isEmpty()) {
            TableView.setItems(FXCollections.observableList(new VisiteursService().getall()));
            return;
        }

        // Filtrer les visiteurs
        ObservableList<visiteurs> filteredList = FXCollections.observableArrayList();
        for (visiteurs v : new VisiteursService().getall()) { // ✅ Correction ici
            if (v.getNom().toLowerCase().contains(searchText) ||
                    v.getPrenom().toLowerCase().contains(searchText) ||
                    String.valueOf(v.getCin()).contains(searchText)) { // ✅ Ajout de la recherche par CIN
                filteredList.add(v);
            }
        }

        TableView.setItems(filteredList);
    }
}
