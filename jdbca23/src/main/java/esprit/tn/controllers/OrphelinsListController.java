package esprit.tn.controllers;

import esprit.tn.entities.Orphelin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import esprit.tn.services.ServiceOrphelin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import esprit.tn.services.QRCodeGenerator;
import java.io.File;



public class OrphelinsListController {

    @FXML
    private FlowPane orphelinsContainer;

    @FXML
    private TextField searchBar;

    private final ServiceOrphelin serviceOrphelin = new ServiceOrphelin();
    private List<Orphelin> allOrphelins;

    @FXML
    public void initialize() {
        afficherOrphelins();
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterOrphelins(newValue));
    }

    private void afficherOrphelins() {
        try {
            allOrphelins = serviceOrphelin.getAllOrphelins();
            mettreAJourAffichage(allOrphelins);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mettreAJourAffichage(List<Orphelin> orphelins) {
        orphelinsContainer.getChildren().clear();

        for (Orphelin o : orphelins) {
            VBox orphelinBox = new VBox();
            orphelinBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #f0f0f0;");
            orphelinBox.setPrefSize(150, 150);

            Label nomPrenom = new Label(o.getNomO() + " " + o.getPrenomO());
            nomPrenom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Générer le QR Code pour cet orphelin
            String qrFilePath = QRCodeGenerator.generateQRCODE(o, "qr_orphelin_" + o.getIdO());

            ImageView qrView = new ImageView();
            if (qrFilePath != null) {
                Image qrImage = new Image(new File(qrFilePath).toURI().toString());
                qrView.setImage(qrImage);
                qrView.setFitWidth(80);
                qrView.setFitHeight(80);
            }

            orphelinBox.getChildren().addAll(nomPrenom, qrView);
            orphelinBox.setOnMouseClicked(event -> afficherDetailsOrphelin(o));
            orphelinsContainer.getChildren().add(orphelinBox);
        }
    }

    private void filterOrphelins(String query) {
        if (query == null || query.trim().isEmpty()) {
            mettreAJourAffichage(allOrphelins);
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<Orphelin> filteredOrphelins = allOrphelins.stream()
                .filter(o -> o.getNomO().toLowerCase().contains(lowerCaseQuery) ||
                        o.getPrenomO().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        mettreAJourAffichage(filteredOrphelins);
    }

    private void afficherDetailsOrphelin(Orphelin orphelin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrphelinDetails.fxml"));
            Parent root = loader.load();

            OrphelinDetailsController controller = loader.getController();
            controller.setOrphelin(orphelin);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de l'orphelin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

