package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import esprit.tn.services.ServiceCours;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TuteurDashboardController {

    @FXML
    private TableView<Cours> tableCours;
    @FXML
    private TableColumn<Cours, String> colTitre;
    @FXML
    private TableColumn<Cours, ImageView> colImage;
    @FXML
    private Button btnDetails;

    private final ServiceCours serviceCours = new ServiceCours();
    private ObservableList<Cours> listeCours;

    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés des cours
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colImage.setCellValueFactory(cellData -> {
            String imagePath = cellData.getValue().getImageC();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/" + imagePath)));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        // Charger les cours
        chargerCours();

    }

    private void chargerCours() {
        List<Cours> coursList = serviceCours.afficherCours();
        tableCours.getItems().setAll(coursList);
    }

    @FXML
    private void afficherDetailsCours() {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();
        if (coursSelectionne != null) {
            try {
                DetailsCoursTuteurController.setCoursSelectionne(coursSelectionne);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsCoursTuteur.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Détails du Cours");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un cours.");
            alert.show();
        }
    }

    @FXML
    void quitter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginTuteur.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Tuteur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void AjouterCours(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCoursD.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("L'ajout d'un cours");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void modifier(ActionEvent event) {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();
        if (coursSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un cours !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCoursD.fxml"));
            Parent root = loader.load();

            ModifierCoursControllerD controller = loader.getController();
            controller.setCours(coursSelectionne);
            controller.setMainController(this);

            // Remplacer le contenu actuel par ModifierCours.fxml
            tableCours.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void supprimer() {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();

        if (coursSelectionne != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le cours");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce cours ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                serviceCours.supprimerCoursD(coursSelectionne.getIdC());
                chargerCours();  // Mettre à jour la table après suppression
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun cours sélectionné");
            alert.setHeaderText("Suppression impossible");
            alert.setContentText("Veuillez sélectionner un cours avant de supprimer.");
            alert.showAndWait();
        }
    }
}

