package esprit.tn.controllers;

import esprit.tn.entities.MailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class Test {

    @FXML
    private void handleMailButton(ActionEvent actionEvent) {
        String recipientEmail = "elamari.louay@esprit.tn";  // Exemple d'email du destinataire
        String verificationCode = "123456";  // Exemple de code de vérification

        try {
            // Envoi de l'email via la méthode SMTP
            MailService.send(recipientEmail, verificationCode);
            showAlert("Succès", "E-mail envoyé à " + recipientEmail, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Échec de l'envoi de l'email : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
