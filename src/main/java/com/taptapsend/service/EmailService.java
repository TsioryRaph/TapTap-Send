package com.taptapsend.service;

import com.taptapsend.model.Client;
import com.taptapsend.model.Envoyer;
import com.taptapsend.dao.ClientDAO;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private final String username = "votre_email@gmail.com";
    private final String password = "votre_mot_de_passe_app"; // Utilisez un mot de passe d'application
    private final Properties props;

    public EmailService() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Ajout important pour Java 17+
    }

    public void sendTransferNotification(Envoyer envoyer) {
        ClientDAO clientDAO = new ClientDAO();
        try {
            Client envoyeur = clientDAO.getClientByNumtel(envoyer.getNumEnvoyeur());
            Client recepteur = clientDAO.getClientByNumtel(envoyer.getNumRecepteur());

            if (envoyeur != null && recepteur != null) {
                // Envoyer au client envoyeur
                sendEmail(
                        envoyeur.getMail(),
                        "Confirmation d'envoi d'argent",
                        String.format("""
                        Bonjour %s,
                        
                        Vous avez envoyé %.2f EUR à %s (%s).
                        Raison: %s
                        
                        Cordialement,
                        L'équipe TapTapSend
                        """,
                                envoyeur.getNom(),
                                envoyer.getMontant(),
                                recepteur.getNom(),
                                envoyer.getNumRecepteur(),
                                envoyer.getRaison())
                );

                // Envoyer au client récepteur
                sendEmail(
                        recepteur.getMail(),
                        "Notification de réception d'argent",
                        String.format("""
                        Bonjour %s,
                        
                        Vous avez reçu %.2f EUR de %s (%s).
                        Raison: %s
                        
                        Cordialement,
                        L'équipe TapTapSend
                        """,
                                recepteur.getNom(),
                                envoyer.getMontant(),
                                envoyeur.getNom(),
                                envoyer.getNumEnvoyeur(),
                                envoyer.getRaison())
                );
            }
        } catch (Exception e) {
            e.printStackTrace(); // À remplacer par un vrai système de logs
        }
    }

    private void sendEmail(String to, String subject, String text) {
        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email à " + to, e);
        }
    }
}