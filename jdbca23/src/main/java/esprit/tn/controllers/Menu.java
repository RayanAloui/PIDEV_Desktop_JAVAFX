package esprit.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Menu {

    @FXML
    private AnchorPane mainPane; // Assurez-vous que votre FXML a un AnchorPane avec fx:id="mainPane"

    @FXML
    void carte(ActionEvent event) {
        try {
            // Charger la nouvelle page
            Parent root = FXMLLoader.load(getClass().getResource("/visiteur/CarteIdentite.fxml"));

            // Remplacer le contenu de la fenêtre principale
            mainPane.getChildren().setAll(root);

        } catch (IOException e) {
            afficherErreur("Impossible de charger la page Carte d'Identité", e);
        }
    }

    @FXML
    void manuellement(ActionEvent event) {
        try {
            // Charger la nouvelle page
            Parent root = FXMLLoader.load(getClass().getResource("/visiteur/AjouterVisiteur.fxml"));

            // Remplacer le contenu de la fenêtre principale
            mainPane.getChildren().setAll(root);

        } catch (IOException e) {
            afficherErreur("Impossible de charger la page Ajouter Visiteur", e);
        }
    }

    private void afficherErreur(String message, IOException e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de chargement");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
