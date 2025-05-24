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

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.itextpdf.text.DocumentException;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/envoyer")
public class EnvoyerController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EnvoyerController.class.getName());
    private EnvoyerService envoyerService;
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.envoyerService = new EnvoyerService();
        this.clientService = new ClientService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                redirectToEnvoiList(response);
                return;
            }

            switch (action) {
                case "add":
                    handleAddEnvoi(request, response);
                    break;
                case "update":
                    handleUpdateEnvoi(request, response);
                    break;
                case "delete":
                    handleDeleteEnvoi(request, response);
                    break;
                case "generatePdf":
                    generatePdf(request, response);
                    break;
                default:
                    redirectToEnvoiList(response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du traitement POST", e);
            handleError(request, response, "Erreur lors du traitement: " + e.getMessage());
        }
    }

    @Override
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
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du traitement GET", e);
            handleError(request, response, "Erreur lors de l'affichage: " + e.getMessage());
        }
    }

    private void listEnvois(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Envoyer> envois = envoyerService.getAllEnvois();
            request.setAttribute("envois", envois);
            forwardToView("/WEB-INF/views/envoyer/list.jsp", request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de la récupération des transferts");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Client> clients = clientService.getAllClients();
            request.setAttribute("clients", clients);
            forwardToView("/WEB-INF/views/envoyer/add.jsp", request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de la récupération des clients");
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idEnv = request.getParameter("idEnv");
        if (idEnv == null || idEnv.isEmpty()) {
            redirectToEnvoiList(response);
            return;
        }

        try {
            Envoyer envoi = envoyerService.getEnvoiById(idEnv);
            if (envoi == null) {
                redirectToEnvoiList(response);
                return;
            }

            List<Client> clients = clientService.getAllClients();
            request.setAttribute("envoi", envoi);
            request.setAttribute("clients", clients);
            forwardToView("/WEB-INF/views/envoyer/edit.jsp", request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de la récupération du transfert");
        }
    }

    private void showPdfForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            int currentYear = cal.get(Calendar.YEAR);

            List<Client> clients = clientService.getAllClients();
            request.setAttribute("clients", clients);
            request.setAttribute("currentMonth", currentMonth);
            request.setAttribute("currentYear", currentYear);

            forwardToView("/WEB-INF/views/envoyer/pdfForm.jsp", request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de la préparation du formulaire PDF");
        }
    }

    private void searchByDate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.isEmpty()) {
            listEnvois(request, response);
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateStr);
            Timestamp date = new Timestamp(parsedDate.getTime());

            List<Envoyer> envois = envoyerService.getEnvoisByDate(date);
            request.setAttribute("envois", envois);
            forwardToView("/WEB-INF/views/envoyer/list.jsp", request, response);
        } catch (ParseException e) {
            request.setAttribute("error", "Format de date invalide");
            listEnvois(request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de la recherche par date");
        }
    }

    private void handleAddEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Envoyer newEnvoi = extractEnvoiFromRequest(request);
            envoyerService.addEnvoi(newEnvoi);

            request.getSession().setAttribute("successMessage", "Transfert ajouté avec succès");
            redirectToEnvoiList(response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("envoi", extractEnvoiFromRequest(request));
            } catch (ParseException ex) {
                request.setAttribute("error", "Erreur lors de la récupération des données du formulaire");
            }
            showNewForm(request, response);
        } catch (ParseException e) {
            request.setAttribute("error", "Format de date invalide");
            showNewForm(request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de l'ajout du transfert");
        }
    }

    private void handleUpdateEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Envoyer envoi = extractEnvoiFromRequest(request);
            envoi.setIdEnv(request.getParameter("idEnv"));

            envoyerService.updateEnvoi(envoi);
            request.getSession().setAttribute("successMessage", "Transfert modifié avec succès");
            redirectToEnvoiList(response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("envoi", extractEnvoiFromRequest(request));
            } catch (ParseException ex) {
                request.setAttribute("error", "Erreur lors de la récupération des données du formulaire");
            }
            showEditForm(request, response);
        } catch (ParseException e) {
            request.setAttribute("error", "Format de date invalide");
            showEditForm(request, response);
        } catch (SQLException e) {
            handleError(request, response, "Erreur lors de la modification du transfert");
        }
    }

    private void handleDeleteEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idEnv = request.getParameter("idEnv");
        if (idEnv != null && !idEnv.isEmpty()) {
            try {
                envoyerService.deleteEnvoi(idEnv);
                request.getSession().setAttribute("successMessage", "Transfert supprimé avec succès");
            } catch (SQLException e) {
                handleError(request, response, "Erreur lors de la suppression du transfert");
            }
        }
        redirectToEnvoiList(response);
    }

    private Envoyer extractEnvoiFromRequest(HttpServletRequest request) throws ParseException {
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        String raison = request.getParameter("raison");
        String dateStr = request.getParameter("date");

        int montant;
        try {
            montant = Integer.parseInt(request.getParameter("montant"));
            if (montant <= 0) {
                throw new IllegalArgumentException("Le montant doit être positif");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Montant invalide");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parsedDate = dateFormat.parse(dateStr);
        Timestamp date = new Timestamp(parsedDate.getTime());

        Envoyer envoi = new Envoyer();
        envoi.setNumEnvoyeur(numEnvoyeur);
        envoi.setNumRecepteur(numRecepteur);
        envoi.setMontant(montant);
        envoi.setRaison(raison);
        envoi.setDate(date);

        return envoi;
    }

    private void generatePdf(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String numtel = request.getParameter("numtel");
            int month = Integer.parseInt(request.getParameter("month"));
            int year = Integer.parseInt(request.getParameter("year"));

            if (month < 1 || month > 12) {
                throw new ServletException("Mois invalide");
            }

            Client client = clientService.getClientByNumtel(numtel);
            if (client == null) {
                throw new ServletException("Client non trouvé");
            }

            List<Envoyer> operations = envoyerService.getEnvoisByClientAndMonth(numtel, month, year);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"releve_operations.pdf\"");

            try (OutputStream out = response.getOutputStream()) {
                PDFGenerator.generateOperationStatement(client, operations, month, year, out);
            } catch (DocumentException e) {
                throw new ServletException("Erreur lors de la création du PDF", e);
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Format de mois ou année invalide", e);
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }
    }

    private void forwardToView(String viewPath, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void redirectToEnvoiList(HttpServletResponse response) throws IOException {
        response.sendRedirect("envoyer");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        try {
            listEnvois(request, response);
        } catch (Exception e) {
            throw new ServletException("Erreur critique", e);
        }
    }
}