package esprit.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import esprit.tn.services.StripeService;
import java.awt.Desktop;
import java.net.URL;

public class PayementStripeController {

    @FXML
    private TextField montantField;
    @FXML
    private TextField telephoneField;
    @FXML
    private Label montantLabel;

    public void setMontant(double montant) {
        this.montantField.setText(String.valueOf(montant));
    }
    @FXML
    public void initialize() {
        System.out.println("Controller initialized");
        System.out.println("telephoneField: " + telephoneField);
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

            // Récupérer le numéro de téléphone entré
            String telephone = telephoneField.getText();
            if (telephone.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un numéro de téléphone valide.");
                return;
            }

            // 🔹 Appeler Stripe pour créer une session de paiement
            String paymentLink = StripeService.createCheckoutSession(montant);
            if (paymentLink != null && paymentLink.startsWith("http")) {
                System.out.println("Lien de paiement Stripe : " + paymentLink);

                // Ouvrir le lien dans le navigateur
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URL(paymentLink).toURI());
                } else {
                    showAlert("Lien de paiement", "Ouvrez ce lien pour payer : " + paymentLink);
                }

                // 🔹 Envoyer un message WhatsApp après le paiement
                StripeService.sendWhatsAppConfirmation(telephone);

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
