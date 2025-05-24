package com.taptapsend.util;

import com.taptapsend.model.Client;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    private final String username = "your.email@gmail.com"; // Remplacez par votre email
    private final String password = "yourpassword"; // Remplacez par votre mot de passe

    public void sendTransferEmail(Client envoyeur, Client recepteur, int montantEnvoye, int montantRecu, int frais) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Important pour Java 17+

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Email pour l'envoyeur
            sendEmail(session, envoyeur.getMail(),
                    "Confirmation d'envoi d'argent",
                    String.format("Bonjour %s,%n%nVous avez envoyé %d à %s.%nFrais d'envoi: %d%nMontant reçu par le destinataire: %d%n%nCordialement,%nL'équipe TapTap Send",
                            envoyeur.getNom(), montantEnvoye, recepteur.getNom(), frais, montantRecu));

            // Email pour le récepteur
            sendEmail(session, recepteur.getMail(),
                    "Notification de réception d'argent",
                    String.format("Bonjour %s,%n%nVous avez reçu %d de %s.%nRaison: Transfert d'argent%n%nCordialement,%nL'équipe TapTap Send",
                            recepteur.getNom(), montantRecu, envoyeur.getNom()));

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi des emails", e);
        }
    }

    private void sendEmail(Session session, String to, String subject, String text)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }
}