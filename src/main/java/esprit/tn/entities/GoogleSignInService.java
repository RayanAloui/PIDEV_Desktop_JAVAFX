package esprit.tn.entities;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

public class GoogleSignInService {

    // Path to your client_secret.json file
    private static final String CLIENT_SECRET_FILE = "/client_secret.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String REDIRECT_URI = "http://localhost:8888"; // Local redirect URI

    public static Userinfo authenticateUser() throws IOException {
        NetHttpTransport httpTransport = new NetHttpTransport();

        // Load client secrets from the client_secret.json file
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(GoogleSignInService.class.getResourceAsStream(CLIENT_SECRET_FILE)));

        // Create the authorization flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singletonList("https://www.googleapis.com/auth/userinfo.profile"))
                .setAccessType("offline")
                .build();

        // Start a local server to receive the callback with the authorization code
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        // Use the credential to fetch user info
        Oauth2 oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("Google OAuth JavaFX")
                .build();

        return oauth2.userinfo().get().execute(); // Get user info from Google
    }
}



