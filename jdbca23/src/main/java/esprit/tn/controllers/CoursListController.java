package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Tuteur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import esprit.tn.services.ServiceCours;
import esprit.tn.services.ServiceTuteur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CoursListController implements Initializable {

    @FXML
    private TableView<Cours> tableCours;
    @FXML
    private TableColumn<Cours, String> colTitre;
    @FXML
    private TableColumn<Cours, String> colContenu;
    @FXML
    private TableColumn<Cours, String> colTuteur;
    @FXML
    private TableColumn<Cours, ImageView> colImage;
    @FXML
    private TableColumn<Cours, String> colResume;

    private final ServiceCours serviceCours = new ServiceCours();
    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colResume.setCellValueFactory(new PropertyValueFactory<>("resume"));
        colTuteur.setCellValueFactory(cellData -> {
            int idTuteur = cellData.getValue().getIdTuteur();
            Tuteur tuteur = null;
            try {
                tuteur = serviceTuteur.getTuteurById(idTuteur);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new javafx.beans.property.SimpleStringProperty(tuteur.getNomT() + " " + tuteur.getPrenomT());
        });

        colImage.setCellValueFactory(cellData -> {
            String imagePath = cellData.getValue().getImageC();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/" + imagePath)));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        afficherCours();
    }


    private void afficherCours() {
        List<Cours> coursList = serviceCours.afficherCours();
        tableCours.getItems().setAll(coursList);
    }

    @FXML
    private void ouvrirFenetreModification() {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();
        if (coursSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un cours !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCours.fxml"));
            Parent root = loader.load();

            ModifierCoursController controller = loader.getController();
            controller.setCours(coursSelectionne);
            controller.setMainController(this);

            // Remplacer le contenu actuel par ModifierCours.fxml
            tableCours.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chargerListeCours() {
        List<Cours> coursList = serviceCours.afficherCours();
        tableCours.getItems().setAll(coursList);
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void supprimerCours() {
        Cours coursSelectionne = tableCours.getSelectionModel().getSelectedItem();

        if (coursSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un cours à supprimer.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce cours ?");

        Optional<ButtonType> resultat = confirmation.showAndWait();
        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            ServiceCours serviceCours = new ServiceCours();
            serviceCours.supprimerCours(coursSelectionne.getIdC()); // Suppression dans la BD

            // Mettre à jour la liste après suppression
            tableCours.getItems().remove(coursSelectionne);
        }
    }

    @FXML
    public void AjouterCours(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCours.fxml"));
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
    void AfficherTuteurs(ActionEvent event) {
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
    void AfficherOrphelins(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOrphelin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Orphelins");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

