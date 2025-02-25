package visites.tn.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import visites.tn.entities.visiteurs;
import visites.tn.services.VisiteursService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarteIdentite {

    @FXML
    private Label nomfichier;

    private String cheminFichier;

    @FXML
    void importerFichier(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image de carte d'identité");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) nomfichier.getScene().getWindow(); // Récupérer la fenêtre actuelle
        File fichierSelectionne = fileChooser.showOpenDialog(stage);

        if (fichierSelectionne != null) {
            cheminFichier = fichierSelectionne.getAbsolutePath();
            nomfichier.setText(fichierSelectionne.getName()); // Afficher le nom du fichier sélectionné
        }
    }

    @FXML
    void connecter(ActionEvent event) {
        if (cheminFichier == null || cheminFichier.isEmpty()) {
            showError("Veuillez d'abord sélectionner une image !");
            return;
        }

        File imageFile = new File(cheminFichier);
        if (!imageFile.exists()) {
            showError("L'image sélectionnée n'existe pas !");
            return;
        }

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata/"); // Modifier si nécessaire
        tesseract.setLanguage("eng"); // "fra" si la carte est en français

        try {
            // 🔹 Extraction du texte via OCR
            String extractedText = tesseract.doOCR(imageFile);

            // 🔹 Détection du CIN (8 chiffres)
            Pattern pattern = Pattern.compile("\\b\\d{8}\\b");
            Matcher matcher = pattern.matcher(extractedText);

            if (matcher.find()) {
                String cinStr = matcher.group(); // Extraction du CIN

                VisiteursService vs = new VisiteursService();
                visiteurs visiteur = vs.rechercherParCIN(cinStr);

                if (visiteur != null) {
                    showInfo("Connexion réussie", "Bienvenue " + visiteur.getNom() + " " + visiteur.getPrenom() + " !");

                    // 🔹 Ouvrir la page AjouterVisite et passer l'ID du visiteur
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/visite/AjouterVisite.fxml"));
                    Parent root = loader.load();

                    // Récupérer le contrôleur de la nouvelle page
                    AjouterVisite controller = loader.getController();
                    controller.initData(visiteur.getId()); // Passer l'ID du visiteur

                    // Changer la scène
                    Stage stage = (Stage) nomfichier.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    showError("Aucun visiteur trouvé avec ce CIN !");
                }
            } else {
                showError("Aucun CIN valide détecté !");
            }
        } catch (TesseractException | IOException e) {
            showError("Erreur : " + e.getMessage());
        }
    }


    private void showInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
