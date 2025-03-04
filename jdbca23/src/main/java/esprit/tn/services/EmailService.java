package esprit.tn.services;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailTo;
import sibModel.SendSmtpEmailSender;

import java.util.ArrayList;
import java.util.List;

public class EmailService {

    private final String API_KEY = "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"; // Remplace par ta clé API
    //String API_KEY = System.getenv("SENDINBLUE_API_KEY");

    public EmailService() {
        // Configurer l'API client avec la clé API
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setApiKey(API_KEY);
    }

    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        try {
            // Créer une instance de l'API TransactionalEmails
            TransactionalEmailsApi api = new TransactionalEmailsApi();

            // Configurer l'expéditeur
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("ahmedaloui577@gmail.com"); // Remplace par ton email d'expéditeur
            sender.setName("SignLearn");

            // Configurer le destinataire
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(destinataire);

            List<SendSmtpEmailTo> toList = new ArrayList<>();
            toList.add(to);

            // Configurer l'email
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setSubject(sujet);
            sendSmtpEmail.setHtmlContent(contenu);

            // Envoyer l'email
            api.sendTransacEmail(sendSmtpEmail);
            System.out.println("Email envoyé avec succès à " + destinataire);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
}
