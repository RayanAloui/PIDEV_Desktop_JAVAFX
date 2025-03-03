package esprit.tn.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StripeService {

    static {
        Stripe.apiKey = "sk_test_51QvxwLFgrJdVJiBv771GwUXqxgqPKFYlMy6ndnE1pZRq5vQ2N667GLGRny5DPpqf1bNe4YlcM778arXjrw7SzwGP00Ii0nVFyY";    }

    public static String createCheckoutSession(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }

        try {
            // Construire les paramètres de la session Stripe
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://votre-site.com/success")
                    .setCancelUrl("https://votre-site.com/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amount) // Montant en cents
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Donation")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(1L)
                                    .build()
                    )
                    .build();

            // Créer la session Stripe
            Session session = Session.create(params);
            return session.getUrl(); // Retourne le lien de paiement

        } catch (StripeException e) {
            e.printStackTrace();
            return "Erreur lors de la création de la session de paiement : " + e.getMessage();
        }
    }
    public static void sendWhatsAppConfirmation(String phoneNumber) {
        try {
            String apiKey = "4323731"; // Remplace par ta clé API CallMeBot
            String message = "Votre paiement a été reçu avec succès. Merci pour votre don !";
            String encodedMessage = URLEncoder.encode(message, "UTF-8");

            String urlString = "https://api.callmebot.com/whatsapp.php?phone=" + phoneNumber + "&text=" + encodedMessage + "&apikey=" + apiKey;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                System.out.println("✅ Message WhatsApp envoyé à " + phoneNumber);
            } else {
                System.out.println("❌ Échec de l'envoi du message WhatsApp. Code : " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}