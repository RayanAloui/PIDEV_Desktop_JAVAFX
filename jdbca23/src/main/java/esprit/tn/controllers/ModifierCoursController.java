package esprit.tn.controllers;

import esprit.tn.entities.Cours;
import esprit.tn.entities.Tuteur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import esprit.tn.services.ServiceCours;
import esprit.tn.services.ServiceTuteur;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierCoursController implements Initializable {

    @FXML
    private TextField txtTitre;
    @FXML
    private TextArea txtContenu;
    @FXML
    private ComboBox<Tuteur> comboTuteur;
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnImage, btnModifier, btnAnnuler;

    private final ServiceCours serviceCours = new ServiceCours();
    private final ServiceTuteur serviceTuteur = new ServiceTuteur();
    private Cours coursSelectionne;
    private String imagePath;
    private CoursListController mainController;

    public void setMainController(CoursListController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerTuteurs();

        btnImage.setOnAction(event -> choisirImage());
        btnModifier.setOnAction(event -> modifierCours());
    }


    public void setCours(Cours cours) {
        this.coursSelectionne = cours;
        txtTitre.setText(cours.getTitre());
        txtContenu.setText(cours.getContenu());
        imagePath = cours.getImageC();
        imageView.setImage(new Image(getClass().getResourceAsStream("/" + imagePath)));

        // Sélectionner le tuteur correspondant
        for (Tuteur tuteur : comboTuteur.getItems()) {
            if (tuteur.getIdT() == cours.getIdTuteur()) {
                comboTuteur.setValue(tuteur);
                break;
            }
        }
    }

    private void chargerTuteurs() {
        List<Tuteur> tuteurs = serviceTuteur.afficherTuteurs(); // Récupérer tous les tuteurs

        // Afficher uniquement le nom et prénom dans le ComboBox
        comboTuteur.setCellFactory(param -> new ListCell<Tuteur>() {
            @Override
            protected void updateItem(Tuteur tuteur, boolean empty) {
                super.updateItem(tuteur, empty);
                if (empty || tuteur == null) {
                    setText(null);
                } else {
                    setText(tuteur.getNomT() + " " + tuteur.getPrenomT());
                }
            }
        });

        // Définir le format d'affichage quand un tuteur est sélectionné
        comboTuteur.setConverter(new StringConverter<Tuteur>() {
            @Override
            public String toString(Tuteur tuteur) {
                return (tuteur != null) ? tuteur.getNomT() + " " + tuteur.getPrenomT() : "";
            }

            @Override
            public Tuteur fromString(String string) {
                return null; // Non utilisé
            }
        });

        comboTuteur.getItems().setAll(tuteurs); // Ajouter les tuteurs au ComboBox
    }


    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imagePath = "images/" + file.getName(); // Suppose que l'image sera enregistrée dans `resources`
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void modifierCours() {
        if (coursSelectionne == null) {
            return;
        }

        String titre = txtTitre.getText();
        String contenu = txtContenu.getText();
        Tuteur tuteur = comboTuteur.getValue();

        if (titre.isEmpty() || contenu.isEmpty() || tuteur == null || imagePath == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        coursSelectionne.setTitre(titre);
        coursSelectionne.setContenu(contenu);
        coursSelectionne.setIdTuteur(tuteur.getIdT());
        coursSelectionne.setImageC(imagePath);

        serviceCours.modifierCours(coursSelectionne);
        showAlert("Succès", "Cours modifié avec succès !");

        // Revenir à la liste des cours
        retournerALaListe();
    }

    @FXML
    private void retournerALaListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursList.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer son contenu
            Stage stage = (Stage) txtTitre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page CoursList.fxml : " + e.getMessage());
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        txtTitre.getScene().getWindow().hide();
    }
}

