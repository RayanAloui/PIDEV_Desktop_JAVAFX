package esprit.tn.entities;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class EmailService {

    private final String SMTP_SERVER = "smtp-relay.brevo.com";
    private final int SMTP_PORT = 587;
    private final String SMTP_USER = "866fc7002@smtp-brevo.com"; // Identifiant
    private final String SMTP_PASSWORD = "jA8yf9S0rB4UQgFE"; // cle SMTP
    private final String SENDER_EMAIL = "orphelincare@gmail.com"; // Email d'expéditeur
    private final String SENDER_NAME = "OrphenCare";

    public EmailService() {
        // Initialization or any session-related setup can go here
    }

    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        try {
            // Configurer les propriétés de la connexion SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.host", SMTP_SERVER);
            properties.put("mail.smtp.port", SMTP_PORT);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Créer une session de mail avec authentification
            javax.mail.Session mailSession = javax.mail.Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                }
            });

            // Créer l'objet message
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setContent(contenu, "text/html");

            // Envoyer l'email
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + destinataire);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Erreur de codage de l'email", e);
        }
    }
}
