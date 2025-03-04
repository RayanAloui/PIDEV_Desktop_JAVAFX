package esprit.tn.entities;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class SmsService {

    private static final String API_URL = "https://e59dyr.api.infobip.com/sms/1/text/single";  // Use Infobip's correct endpoint
    private static final String API_KEY = "";  // Your Infobip API key

    public static void send(String recipientPhoneNumber, String verificationCode) throws Exception {
        // Prepare the JSON payload
        String payload = "{"
                + "\"from\":\"InfoSMS\","
                + "\"to\":\"" + recipientPhoneNumber + "\","
                + "\"text\":\"Your verification code is: " + verificationCode + "\""
                + "}";

        // Create the URL for the request
        URL url = new URL(API_URL);

        // Open a connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "App " + API_KEY);  // Add API Key for authentication
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Write the payload to the request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();

        // If the response code is 200, the SMS was sent successfully
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println("SMS sent successfully: " + content.toString());
            }
        } else {
            throw new Exception("Error in sending SMS. Response code: " + responseCode);
        }
    }
}
