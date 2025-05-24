package com.taptapsend.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.taptapsend.model.Client;
import com.taptapsend.model.Envoyer;

import java.io.OutputStream;
import java.util.List;

public class PDFGenerator {
    public static void generateOperationStatement(Client client, List<Envoyer> operations,
                                                  int month, int year, OutputStream out) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Titre du document
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Relevé d'opération", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Période
        String[] monthNames = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        Font periodFont = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
        Paragraph period = new Paragraph("Date : " + monthNames[month-1] + " " + year, periodFont);
        period.setSpacingAfter(10);
        document.add(period);

        // Informations client
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        document.add(new Paragraph("Contact : " + client.getNumtel(), infoFont));
        document.add(new Paragraph(client.getNom(), infoFont));
        document.add(new Paragraph("Solde actuel : " + client.getSolde() + " Euros", infoFont));
        document.add(new Paragraph(" "));

        // Tableau des opérations
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // En-têtes du tableau
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        PdfPCell cell1 = new PdfPCell(new Phrase("Date", headerFont));
        PdfPCell cell2 = new PdfPCell(new Phrase("Raison", headerFont));
        PdfPCell cell3 = new PdfPCell(new Phrase("Nom du récepteur", headerFont));
        PdfPCell cell4 = new PdfPCell(new Phrase("Montant", headerFont));

        cell1.setBackgroundColor(BaseColor.GRAY);
        cell2.setBackgroundColor(BaseColor.GRAY);
        cell3.setBackgroundColor(BaseColor.GRAY);
        cell4.setBackgroundColor(BaseColor.GRAY);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);

        // Remplissage du tableau
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        for (Envoyer op : operations) {
            table.addCell(new Phrase(op.getDate().toString(), cellFont));
            table.addCell(new Phrase(op.getRaison(), cellFont));
            table.addCell(new Phrase(op.getNomRecepteur(), cellFont));
            table.addCell(new Phrase(String.valueOf(op.getMontant()), cellFont));
        }

        document.add(table);
        document.close();
    }
}