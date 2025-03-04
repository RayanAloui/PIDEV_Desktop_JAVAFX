package reclamations.services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.List;
public class SmsService {

    // Your Twilio Account SID and Auth Token
    private static final String ACCOUNT_SID = "ACb79aa58f8f4a07be52f63b1d0e4fa661";
    private static final String AUTH_TOKEN = "0a4dc08f609b811f2a859ac37e94dc06";

    // Twilio phone number
    private static final String TWILIO_PHONE_NUMBER = "+17753827580"; // Twilio number you purchased

    // Initialize Twilio with SID and Auth Token
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // Function to send SMS to multiple recipients
    public static void envoyerNotificationSMS(List<String> numerosTuteurs, String message) {
        for (String numero : numerosTuteurs) {
            try {
                envoyerSMS(numero, message);
            } catch (Exception e) {
                System.err.println("Erreur d'envoi SMS à " + numero + " : " + e.getMessage());
            }
        }
    }

    // Function to send a single SMS
    public static void envoyerSMS(String recipientPhoneNumber, String message) throws Exception {
        try {
            // Create and send SMS message using Twilio API
            Message messageSent = Message.creator(
                    new PhoneNumber(recipientPhoneNumber), // Recipient's phone number
                    new PhoneNumber(TWILIO_PHONE_NUMBER),  // Your Twilio phone number
                    message  // The message you want to send
            ).create();

            // Output the SID of the sent message (for debugging purposes)
            System.out.println("SMS envoyé à " + recipientPhoneNumber + " : SID " + messageSent.getSid());

        } catch (Exception e) {
            throw new Exception("Erreur d'envoi SMS à " + recipientPhoneNumber + " : " + e.getMessage());
        }
    }
}
