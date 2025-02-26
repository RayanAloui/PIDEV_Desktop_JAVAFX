package Donateur.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Donateur.tn.services.StripeService;
import java.awt.Desktop;
import java.net.URL;

public class PayementStripeController {

    @FXML
    private TextField montantField;
    @FXML
    private Label montantLabel;

    public void setMontant(double montant) {
        this.montantField.setText(String.valueOf(montant));
    }

    @FXML
    private void handlePaiement() {
        try {
            // Vérifier que le montant est valide
            String montantStr = montantField.getText();
            if (montantStr.isEmpty() || !montantStr.matches("\\d+(\\.\\d+)?")) {
                showAlert("Erreur", "Veuillez entrer un montant valide (exemple : 10.50).");
                return;
            }

            double montantDouble = Double.parseDouble(montantStr);
            long montant = (long) (montantDouble * 100); // Convertir en cents pour Stripe

            // Appeler le service Stripe pour créer une session de paiement
            String paymentLink = StripeService.createCheckoutSession(montant);
            if (paymentLink != null && paymentLink.startsWith("http")) {
                System.out.println("Lien de paiement Stripe : " + paymentLink);

                // Ouvrir le lien dans le navigateur
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URL(paymentLink).toURI());
                } else {
                    showAlert("Lien de paiement", "Ouvrez ce lien pour payer : " + paymentLink);
                }
            } else {
                showAlert("Erreur", paymentLink); // Afficher le message d'erreur
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}