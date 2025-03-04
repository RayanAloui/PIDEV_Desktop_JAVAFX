package esprit.tn.controllers;

import esprit.tn.entities.Tuteur;
import esprit.tn.services.ServiceTuteur;
import esprit.tn.services.QRCodeGenerator;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TuteursListController {

    @FXML
    private FlowPane tuteursContainer;

    @FXML
    private TextField searchBar; // Champ de recherche

    private final ServiceTuteur serviceTuteur = new ServiceTuteur();
    private List<Tuteur> allTuteurs; // Liste complète des tuteurs

    @FXML
    public void initialize() {
        afficherTuteurs();

        // Ajout d'un écouteur sur la barre de recherche
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterTuteurs(newValue));
    }

    private void afficherTuteurs() {
        try {
            allTuteurs = serviceTuteur.getAllTuteurss(); // Récupérer tous les tuteurs
            mettreAJourAffichage(allTuteurs);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des tuteurs : " + e.getMessage());
        }
    }

    /*private void mettreAJourAffichage(List<Tuteur> tuteurs) {
        tuteursContainer.getChildren().clear();

        for (Tuteur t : tuteurs) {
            VBox tuteurBox = new VBox();
            tuteurBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f0f0f0;");
            tuteurBox.setPrefSize(150, 180);

            Label nomPrenom = new Label(t.getNomT() + " " + t.getPrenomT());
            nomPrenom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Générer le QR Code contenant idT, cinT et téléphoneT
            String qrData = t.getIdT() + "-" + t.getCinT() + "-" + t.getTelephoneT();
            String qrPath = QRCodeGenerator.generateQRCode(qrData, "QR_" + t.getIdT());

            // Afficher le QR Code
            ImageView qrImageView = new ImageView(new Image(new File(qrPath).toURI().toString()));
            qrImageView.setFitWidth(100);
            qrImageView.setFitHeight(100);

            tuteurBox.getChildren().addAll(nomPrenom, qrImageView);

            // Action au clic
            tuteurBox.setOnMouseClicked(event -> afficherDetailsTuteur(t));

            tuteursContainer.getChildren().add(tuteurBox);
        }
    }*/

    private void mettreAJourAffichage(List<Tuteur> tuteurs) {
        tuteursContainer.getChildren().clear();

        for (Tuteur t : tuteurs) {
            VBox tuteurBox = new VBox();
            tuteurBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f0f0f0;");
            tuteurBox.setPrefSize(150, 120);

            Label nomPrenom = new Label(t.getNomT() + " " + t.getPrenomT());
            nomPrenom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Générer le QR code et l'afficher
            String qrCodePath = QRCodeGenerator.generateQRCode(t, "qr_" + t.getCinT());
            ImageView qrImageView = new ImageView(new Image("file:" + qrCodePath));
            qrImageView.setFitWidth(100);
            qrImageView.setFitHeight(100);

            tuteurBox.getChildren().addAll(nomPrenom, qrImageView);

            // Action au clic
            tuteurBox.setOnMouseClicked(event -> afficherDetailsTuteur(t));

            tuteursContainer.getChildren().add(tuteurBox);
        }
    }


    private void filterTuteurs(String query) {
        if (query == null || query.trim().isEmpty()) {
            mettreAJourAffichage(allTuteurs);
            return;
        }

        // Convertir la recherche en minuscule pour éviter les erreurs de casse
        String lowerCaseQuery = query.toLowerCase();

        List<Tuteur> filteredTuteurs = allTuteurs.stream()
                .filter(t -> String.valueOf(t.getIdT()).contains(lowerCaseQuery) ||  // Recherche par ID
                        t.getCinT().toLowerCase().contains(lowerCaseQuery) ||   // Recherche par CIN
                        t.getTelephoneT().toLowerCase().contains(lowerCaseQuery)) // Recherche par téléphone
                .collect(Collectors.toList());

        mettreAJourAffichage(filteredTuteurs);
    }

    private void afficherDetailsTuteur(Tuteur tuteur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TuteurDetails.fxml"));
            Parent root = loader.load();

            TuteurDetailsController controller = loader.getController();
            controller.setTuteur(tuteur); // Passer le tuteur sélectionné

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du tuteur");
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
            stage.setTitle("Liste des Orphelins");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



