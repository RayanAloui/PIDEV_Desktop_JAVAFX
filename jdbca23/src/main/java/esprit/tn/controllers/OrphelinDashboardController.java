package esprit.tn.controllers;

import esprit.tn.entities.Orphelin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import esprit.tn.entities.Cours;
import esprit.tn.services.ServiceCours;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OrphelinDashboardController {

    @FXML
    private VBox vboxCours;
    private Cours currentCours;
    private final ServiceCours coursService = new ServiceCours();
    private int idTuteur; // ID du tuteur de l'orphelin connecté
    private Orphelin orphelinActuel; // Stocke l'orphelin sélectionné

    public void setIdTuteur(int idTuteur) {
        this.idTuteur = idTuteur;
        afficherListeCours(); // Charger les cours après avoir défini l'ID du tuteur
    }

    public void setOrphelinActuel(Orphelin orphelin) {
        if (orphelin == null) {
            showAlert("Erreur", "Aucun orphelin sélectionné !");
            return;
        }
        this.orphelinActuel = orphelin;
        this.idTuteur = orphelin.getIdTuteur(); // Récupérer l'ID du tuteur depuis l'orphelin
        System.out.println("✅ Orphelin sélectionné : " + orphelin.getNomO() + " (ID: " + orphelin.getIdO() + ")");
        afficherListeCours();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void initialize() {

    }

    private void afficherListeCours() {
        vboxCours.getChildren().clear(); // Nettoyer les anciens cours affichés

        // Récupérer uniquement les cours du tuteur de l'orphelin
        List<Cours> coursList = coursService.getCoursByTuteur(idTuteur);

        for (Cours cours : coursList) {
            HBox card = new HBox(20);
            card.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-background-radius: 10; -fx-border-radius: 10;");

            Label lblTitre = new Label(cours.getTitre());
            lblTitre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Correction du chargement de l'image
            String imagePath = cours.getImageC();
            ImageView imgView = new ImageView();
            imgView.setFitWidth(100);
            imgView.setFitHeight(100);

            InputStream imageStream = getClass().getResourceAsStream("/" + imagePath);
            if (imageStream != null) {
                imgView.setImage(new Image(imageStream));
            } else {
                System.out.println("⚠ Image non trouvée : " + imagePath);
            }

            card.getChildren().addAll(imgView, lblTitre);

            // Clic sur une carte pour voir les détails
            card.setOnMouseClicked((MouseEvent event) -> ouvrirDetailsCours(cours));

            vboxCours.getChildren().add(card);
        }
    }


    @FXML
    private void ouvrirDetailsCours(Cours selectedCours) {
        if (selectedCours == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un cours.");
            alert.show();
            return;
        }

        if (orphelinActuel == null) { // Vérifier si un orphelin est bien sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING, "Aucun orphelin sélectionné !");
            alert.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsCoursOrphelin.fxml"));
            Parent root = loader.load();

            DetailsCoursOrphelinController controller = loader.getController();

            // Passer le cours et l'ID de l'orphelin au contrôleur
            controller.setIdOrphelin(orphelinActuel.getIdO());
            controller.setCours(selectedCours);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void quitter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginOrphelin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Tuteur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

