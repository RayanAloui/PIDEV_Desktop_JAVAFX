package esprit.tn.services;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class EdenAIService {
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiM2JkZTRjOTgtYzBhOS00NGZkLTgxZDctMGE5ODYyZjE1MTM4IiwidHlwZSI6ImZyb250X2FwaV90b2tlbiJ9.94oC_xmR7LzulXUA1hKwj182LnVzUr6ZbEHhuheAeqg";  // Remplace avec ta clé Eden AI
    //static String API_KEY = System.getenv("OpenAI");

    public static String generateSummary(String courseDescription) {
        try {
            URL url = new URL("https://api.edenai.run/v2/text/generation");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configuration de la requête
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Création du JSON d'entrée avec une consigne explicite
            JSONObject json = new JSONObject();
            json.put("providers", "openai");
            json.put("text", "Résume le texte suivant en quelques phrases : " + courseDescription);
            json.put("temperature", 0.5);
            json.put("max_tokens", 100);

            // Envoi des données
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Récupération de la réponse
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Extraction du résumé généré
                JSONObject responseJson = new JSONObject(response.toString());
                return responseJson.getJSONObject("openai").getString("generated_text").trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la génération du résumé.";
        }
    }
}

