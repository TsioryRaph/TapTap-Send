package com.taptapsend.controller;

import com.taptapsend.model.Client;
import com.taptapsend.model.Envoyer;
import com.taptapsend.service.ClientService;
import com.taptapsend.service.EnvoyerService;
import com.taptapsend.util.PDFGenerator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/envoyer")
public class EnvoyerController extends HttpServlet {
    private final EnvoyerService envoyerService = new EnvoyerService();
    private final ClientService clientService = new ClientService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addEnvoi(request, response);
                    break;
                case "update":
                    updateEnvoi(request, response);
                    break;
                case "delete":
                    deleteEnvoi(request, response);
                    break;
                case "generatePdf":
                    generatePdf(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                listEnvois(request, response);
            } else {
                switch (action) {
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "new":
                        showNewForm(request, response);
                        break;
                    case "searchByDate":
                        searchByDate(request, response);
                        break;
                    case "showPdfForm":
                        showPdfForm(request, response);
                        break;
                    default:
                        listEnvois(request, response);
                        break;
                }
            }
        } catch (SQLException | ParseException e) {
            throw new ServletException(e);
        }
    }

    private void listEnvois(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Envoyer> envois = envoyerService.getAllEnvois();
        request.setAttribute("envois", envois);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/envoyer/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients();
        request.setAttribute("clients", clients);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/envoyer/add.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idEnv = request.getParameter("idEnv");
        Envoyer envoi = envoyerService.getEnvoiById(idEnv);
        List<Client> clients = clientService.getAllClients();

        request.setAttribute("envoi", envoi);
        request.setAttribute("clients", clients);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/envoyer/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void showPdfForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients();
        request.setAttribute("clients", clients);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/envoyer/pdfForm.jsp");
        dispatcher.forward(request, response);
    }

    private void searchByDate(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ParseException {
        String dateStr = request.getParameter("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(dateStr);
        Timestamp date = new Timestamp(parsedDate.getTime());

        List<Envoyer> envois = envoyerService.getEnvoisByDate(date);
        request.setAttribute("envois", envois);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/envoyer/list.jsp");
        dispatcher.forward(request, response);
    }

    private void addEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        String raison = request.getParameter("raison");

        Envoyer newEnvoi = new Envoyer();
        newEnvoi.setNumEnvoyeur(numEnvoyeur);
        newEnvoi.setNumRecepteur(numRecepteur);
        newEnvoi.setMontant(montant);
        newEnvoi.setRaison(raison);

        envoyerService.addEnvoi(newEnvoi);
        response.sendRedirect("envoyer");
    }

    private void updateEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idEnv = request.getParameter("idEnv");
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        String raison = request.getParameter("raison");

        Envoyer envoi = new Envoyer();
        envoi.setIdEnv(idEnv);
        envoi.setNumEnvoyeur(numEnvoyeur);
        envoi.setNumRecepteur(numRecepteur);
        envoi.setMontant(montant);
        envoi.setRaison(raison);

        envoyerService.updateEnvoi(envoi);
        response.sendRedirect("envoyer");
    }

    private void deleteEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idEnv = request.getParameter("idEnv");
        envoyerService.deleteEnvoi(idEnv);
        response.sendRedirect("envoyer");
    }

    private void generatePdf(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String numtel = request.getParameter("numtel");
        int month = Integer.parseInt(request.getParameter("month"));
        int year = Integer.parseInt(request.getParameter("year"));

        Client client = clientService.getClientByNumtel(numtel);
        List<Envoyer> operations = envoyerService.getEnvoisByClientAndMonth(numtel, month, year);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"releve_operations.pdf\"");

        try (OutputStream out = response.getOutputStream()) {
            PDFGenerator.generateOperationStatement(client, operations, month, year, out);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de la génération du PDF", e);
        }
    }
}