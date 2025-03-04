package esprit.tn.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SmsService {

  
    public static void envoyerNotificationSMS(List<String> numerosTuteurs, String message) {
        for (String numero : numerosTuteurs) {
            try {
                envoyerSMS(numero, message);
            } catch (Exception e) {
                System.err.println("Erreur d'envoi SMS à " + numero + " : " + e.getMessage());
            }
        }
    }

    public static void envoyerSMS(String recipientPhoneNumber, String message) throws Exception {
        String payload = "{"
                + "\"from\":\"" + SENDER + "\","
                + "\"to\":\"" + recipientPhoneNumber + "\","
                + "\"text\":\"" + message + "\""
                + "}";

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "App " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println("SMS envoyé à " + recipientPhoneNumber + " : " + content);
            }
        } else {
            throw new Exception("Erreur d'envoi SMS, code réponse : " + responseCode);
        }
    }
}