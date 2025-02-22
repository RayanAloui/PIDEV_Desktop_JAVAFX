package esprit.tn.entities;

import okhttp3.*;

import java.io.IOException;

public class MailService {

    // OkHttpClient instance
    private static final OkHttpClient client = new OkHttpClient();

    // Method to send email using Infobip API
    public void sendEmail(String to, String firstName) {
        // Define media type for the request
        MediaType mediaType = MediaType.parse("multipart/form-data");

        // Build the request body with email data (from, subject, to, placeholders, and text)
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("from", "orphelincare@gmail.com")
                .addFormDataPart("subject", "Free trial")
                .addFormDataPart("to", "{\"to\":\"" + to + "\",\"placeholders\":{\"firstName\":\"" + firstName + "\"}}")
                .addFormDataPart("text", "Hi {{firstName}}, this is a test message from Infobip. Have a nice day!")
                .build();

        // Create request object with the proper API URL and headers
        Request request = new Request.Builder()
                .url("https://8knrdd.api.infobip.com/email/3/send")
                .method("POST", body)
                .addHeader("Authorization", "App cc71286f63ccf3f726a929b0ac93cec2-7f2c14d9-e439-48e4-8b38-8813aaa099b7")
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Accept", "application/json")
                .build();

        // Execute the request and handle the response
        try (Response response = client.newCall(request).execute()) {
            // Print the response code and body for debugging
            System.out.println("Response Code: " + response.code());
            if (response.isSuccessful()) {
                System.out.println("Email sent successfully!");
            } else {
                // Log the detailed error message
                System.out.println("Failed to send email.");
                System.out.println("Error response: " + response.body().string());
            }
        } catch (IOException e) {
            // Catch any IO exceptions and log them
            System.out.println("Error during email sending: " + e.getMessage());
        }
    }


}
