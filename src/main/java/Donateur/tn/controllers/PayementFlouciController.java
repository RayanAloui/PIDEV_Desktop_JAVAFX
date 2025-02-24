package Donateur.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Desktop;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PayementFlouciController {

    @FXML
    private TextField montantField;
    @FXML
    private Label montantLabel;

    // Clés API Flouci (Remplace avec tes vraies clés API)
    private static final String APP_TOKEN = "f47ba6b0-fc34-4726-b62f-c222a6f83d61";
    private static final String APP_SECRET = "1ad923fe-c526-43eb-a975-9ec8cf734f81";
    private static final String API_URL = "https://developers.flouci.com/api/generate_payment";

    public void setMontant(double montant) {
        this.montantField.setText(String.valueOf(montant));
    }


    @FXML
    private void handlePaiement() {
        try {
            // Vérifier que le montant est valide
            String montantStr = montantField.getText();
            if (montantStr.isEmpty() || !montantStr.matches("\\d+")) {
                showAlert("Erreur", "Veuillez entrer un montant valide.");
                return;
            }
            int montant = Integer.parseInt(montantStr) * 1000; // Convertir en millimes

            // Construire les données JSON pour l'API Flouci
            JsonObject data = new JsonObject();
            data.addProperty("app_token", APP_TOKEN);
            data.addProperty("app_secret", APP_SECRET);
            data.addProperty("amount", montant);
            data.addProperty("accept_card", true);
            data.addProperty("success_link", "https://example.com/success");
            data.addProperty("fail_link", "https://example.com/fail");
            data.addProperty("developer_tracking_id", "test_id_123");
            data.addProperty("session_timeout_secs", 1200);

            // Envoyer la requête à Flouci
            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.toString().getBytes("utf-8"));
            }

            int responseCode = conn.getResponseCode();

            System.out.println("Code réponse API : " + responseCode);

// Vérifier si la réponse est une erreur
            if (responseCode != 201) { // 201 = Succès pour Flouci
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    Scanner errorScanner = new Scanner(errorStream);
                    StringBuilder errorResponse = new StringBuilder();
                    while (errorScanner.hasNext()) {
                        errorResponse.append(errorScanner.nextLine());
                    }
                    errorScanner.close();
                    System.out.println("Réponse d'erreur API Flouci : " + errorResponse);
                    showAlert("Erreur API", "Le paiement a échoué. Réponse : " + errorResponse);
                } else {
                    showAlert("Erreur API", "Impossible de récupérer l'erreur détaillée.");
                }
                return;
            }


            // Lire la réponse JSON de Flouci
            InputStream inputStream = conn.getInputStream();
            Scanner scanner = new Scanner(conn.getInputStream());

            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            System.out.println("Réponse complète API : " + response.toString());

            // Parser la réponse JSON
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            if (!jsonResponse.has("result")) {
                showAlert("Erreur API", "La réponse de Flouci ne contient pas 'result'. Vérifiez les logs.");
                return;
            }
            JsonObject result = jsonResponse.getAsJsonObject("result");
            if (!result.has("link")) {
                showAlert("Erreur API", "Flouci n'a pas généré de lien de paiement.");
                return;
            }
            String paymentLink = result.get("link").getAsString();
            System.out.println("Lien de paiement : " + paymentLink);
            // Rediriger l'utilisateur vers la page de paiement Flouci
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URL(paymentLink).toURI());
            } else {
                showAlert("Lien de paiement", "Ouvrez ce lien pour payer : " + paymentLink);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
