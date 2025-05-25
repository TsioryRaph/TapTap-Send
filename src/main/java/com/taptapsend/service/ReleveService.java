package com.taptapsend.service;

import com.taptapsend.dao.ClientDAO;
import com.taptapsend.model.Client;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReleveService {
    private static final Logger logger = Logger.getLogger(ReleveService.class.getName());
    private final ClientDAO clientDAO;

    public ReleveService(ClientDAO clientDAO) {
        this.clientDAO = Objects.requireNonNull(clientDAO, "ClientDAO ne peut pas être null");
    }

    public List<String> getClientsActifs() {
        try {
            List<Client> clients = clientDAO.getAllClients();
            return clients.stream()
                    .filter(client -> client.getSolde() > 0)
                    .map(client -> String.format("%s - %s", client.getNumtel(), client.getNom()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des clients actifs", e);
            return List.of();
        }
    }

    public byte[] genererRelevePDF(String numClient, String periode, String dateDebut, String dateFin) {
        Objects.requireNonNull(numClient, "Le numéro client ne peut pas être null");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Récupération du client avec gestion d'erreur améliorée
            Client client = clientDAO.getAllClients().stream()
                    .filter(c -> numClient.equals(c.getNumtel()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec le numéro: " + numClient));

            // Construction du PDF
            buildPdfContent(document, client, periode, dateDebut, dateFin);

        } catch (DocumentException e) {
            logger.log(Level.SEVERE, "Erreur de document PDF pour client " + numClient, e);
            throw new RuntimeException("Erreur technique lors de la génération du PDF", e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, e.getMessage());
            throw e; // On propage l'erreur métier
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur inattendue lors de la génération du PDF", e);
            throw new RuntimeException("Erreur inattendue", e);
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        return baos.toByteArray();
    }

    private void buildPdfContent(Document document, Client client, String periode,
                                 String dateDebut, String dateFin) throws DocumentException {
        // En-tête
        document.add(new Paragraph("RELEVE CLIENT"));
        document.add(new Paragraph("Période: " + periode));
        document.add(new Paragraph("Client: " + client.getNom()));
        document.add(new Paragraph("Téléphone: " + client.getNumtel()));
        document.add(new Paragraph("Email: " + Optional.ofNullable(client.getMail()).orElse("Non renseigné")));
        document.add(new Paragraph(" "));

        // Solde
        document.add(new Paragraph(String.format("Solde actuel: %d EUR", client.getSolde())));
        document.add(new Paragraph(" "));

        // Section transactions
        document.add(new Paragraph("Transactions entre " + dateDebut + " et " + dateFin + ":"));
        document.add(new Paragraph("Fonctionnalité en cours de développement..."));
    }
}