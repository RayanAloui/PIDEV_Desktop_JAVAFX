package controllers;

import entities.Cours;
import entities.Tuteur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCours;
import services.ServiceTuteur;
import javax.swing.text.AbstractDocument;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AjouterCoursController {

    @FXML
    private TextField txtTitre;
    @FXML
    private TextArea txtContenu;
    @FXML
    private Label lblImagePath;
    @FXML
    private ComboBox<Tuteur> comboTuteur;
    @FXML
    private Button btnChoisirImage;
    @FXML
    private Button btnAjouterCours;

    private String imagePath;
    private final ServiceCours serviceCours = new ServiceCours();
    private final ServiceTuteur serviceTuteur = new ServiceTuteur();

    @FXML
    public void initialize() {
        chargerTuteurs();
    }

    private void chargerTuteurs() {
        List<Tuteur> tuteurs = serviceTuteur.afficherTuteurs();
        comboTuteur.getItems().addAll(tuteurs);

        // Personnaliser l'affichage
        comboTuteur.setCellFactory(param -> new ListCell<>() {
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

        // Pour afficher le nom et prénom aussi après la sélection
        comboTuteur.setButtonCell(new ListCell<>() {
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
    }


    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Définir le dossier cible dans les ressources
                File destinationFolder = new File("src/main/resources/images/");
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs(); // Crée le dossier s'il n'existe pas
                }

                // Définir le fichier de destination
                File destinationFile = new File(destinationFolder, selectedFile.getName());

                // Copier le fichier dans les ressources
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour l'image path pour stockage
                imagePath = "images/" + selectedFile.getName();
                lblImagePath.setText(selectedFile.getName());

            } catch (IOException e) {
                System.out.println("Erreur lors de la copie de l'image : " + e.getMessage());
            }
        }
    }


    @FXML
    private void ajouterCours(ActionEvent event) {
        String titre = txtTitre.getText().trim();
        String contenu = txtContenu.getText().trim();
        Tuteur tuteur = comboTuteur.getValue();

        if (titre.isEmpty() || contenu.isEmpty() || tuteur == null || imagePath == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.show();
            return;
        }

        Cours cours = new Cours(0, titre, contenu, imagePath, tuteur.getIdT());
        serviceCours.ajouterCours(cours);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cours ajouté avec succès !", ButtonType.OK);
        alert.show();

        // Réinitialiser les champs
        txtTitre.clear();
        txtContenu.clear();
        comboTuteur.setValue(null);
        lblImagePath.setText("Aucune image sélectionnée");
        imagePath = null;
    }

    public void AfficherCours(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des cours");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AfficherCoursD(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TuteurDashbord.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des cours");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

