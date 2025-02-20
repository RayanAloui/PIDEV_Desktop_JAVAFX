package esprit.tn.entities;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailService {

    public static void send(String recipientEmail, String verificationCode) {
        // Paramètres SMTP Infobip
        String host = "smtp.infobip.com";  // Serveur SMTP d'Infobip
        final String username = "your_username";  // Votre nom d'utilisateur Infobip
        final String password = "your_password";  // Votre mot de passe Infobip

        // Paramètres de connexion au serveur SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Créer une session avec le serveur SMTP
        javax.mail.Session mailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Créer un objet MimeMessage
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("orphelincare@gmail.com"));  // Adresse de l'expéditeur
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));  // Adresse du destinataire
            message.setSubject("Vérification de votre code");
            message.setText("Bonjour, \n\nVoici votre code de vérification : " + verificationCode);

            // Envoi du message
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }
}
