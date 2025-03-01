package Donateur.tn.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatBotController {
    @FXML
    private TextField userInputField;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea chatArea;

    private static final String EDEN_AI_URL = "https://api.edenai.run/v2/text/generation";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMWEzZmIyNWQtMGUzNi00M2Y4LTk2YzItOTYzNWZiNDMwYjAzIiwidHlwZSI6ImFwaV90b2tlbiJ9.lAS1EGQ6muE4uHste5-xXauIoucFnjGUuU-koUiHiJM"; // Remplace avec ta clé API Eden AI

    // Map pour stocker les questions et réponses personnalisées
    private Map<String, String> customResponses = new HashMap<>();

    public ChatBotController() {
        // Initialiser les questions et réponses personnalisées
        customResponses.put("vous etes qui ?", "Je suis un chatbot, mon role et de vous repondre a tous vos questions à propos l'orphelinat OrphanCare.");
        customResponses.put("c'est quoi OrphanCare ?", "OrphanCare est une initiative dédiée au bien-être des orphelins en Tunisie. Notre mission est d'offrir un environnement sûr et chaleureux aux enfants sans famille, en leur fournissant un hébergement, une éducation et un accompagnement pour leur permettre de s'épanouir. Nous facilitons également les dons et le bénévolat afin que chacun puisse contribuer à améliorer leur avenir. Ensemble, nous construisons un avenir meilleur pour ces enfants.");
        customResponses.put("Qui t'as créé ?", "J'ai été créé par une équipe de développeurs à ESPRIT.");
        // Ajoutez d'autres paires question-réponse ici
    }

    @FXML
    private void sendMessage() {
        String userMessage = userInputField.getText().trim();
        if (userMessage.isEmpty()) return;

        chatArea.appendText("Vous: " + userMessage + "\n");
        userInputField.clear();

        // Vérifier si la question a une réponse personnalisée
        if (customResponses.containsKey(userMessage)) {
            String botReply = customResponses.get(userMessage);
            chatArea.appendText("Chatbot: " + botReply + "\n");
            return; // Sortir de la méthode si une réponse personnalisée est trouvée
        }

        // Si aucune réponse personnalisée n'est trouvée, utiliser l'API Eden AI
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("providers", new String[]{"openai"}); // Utilise OpenAI sur Eden AI
        jsonRequest.put("text", userMessage);
        jsonRequest.put("temperature", 0.5);

        RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(EDEN_AI_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                chatArea.appendText("Chatbot: Erreur de connexion.\n");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    System.out.println("Réponse API Eden AI : " + jsonResponse);

                    // Vérifier si l'objet "openai" existe dans la réponse
                    if (jsonResponse.has("openai")) {
                        JSONObject openAiResponse = jsonResponse.getJSONObject("openai");

                        // Vérifier si "generated_text" existe
                        if (openAiResponse.has("generated_text")) {
                            String botReply = openAiResponse.getString("generated_text");

                            // Afficher la réponse du chatbot
                            chatArea.appendText("Chatbot: " + botReply + "\n");
                        } else {
                            chatArea.appendText("Chatbot: Erreur - Aucun texte généré.\n");
                        }
                    } else {
                        chatArea.appendText("Chatbot: Erreur - Réponse inattendue de l'API.\n");
                    }
                } else {
                    chatArea.appendText("Chatbot: Erreur - Réponse non réussie de l'API.\n");
                }
            }
        });
    }
}
