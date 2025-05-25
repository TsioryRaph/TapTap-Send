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
import java.util.UUID;
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
            logger.log(Level.SEVERE, "Erreur POST action: " + action, e);
            request.getSession().setAttribute("errorMessage", "Erreur: " + e.getMessage());
            redirectToEnvoiList(response);
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
            logger.log(Level.SEVERE, "Erreur GET action: " + action, e);
            request.getSession().setAttribute("errorMessage", "Erreur: " + e.getMessage());
            redirectToEnvoiList(response);
        }
    }

    private void listEnvois(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Envoyer> envois = envoyerService.getAllEnvois();
            logger.log(Level.INFO, "Nombre de transferts chargés: {0}", envois.size());

            request.setAttribute("envois", envois);
            request.setAttribute("today", new Date());

            forwardToView("/WEB-INF/views/envoyer/list.jsp", request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur récupération liste transferts", e);
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
            logger.log(Level.SEVERE, "Erreur récupération clients", e);
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
                request.getSession().setAttribute("errorMessage", "Transfert non trouvé");
                redirectToEnvoiList(response);
                return;
            }

            List<Client> clients = clientService.getAllClients();
            request.setAttribute("envoi", envoi);
            request.setAttribute("clients", clients);
            forwardToView("/WEB-INF/views/envoyer/edit.jsp", request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur récupération transfert ID: " + idEnv, e);
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
            logger.log(Level.SEVERE, "Erreur préparation formulaire PDF", e);
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
            request.setAttribute("searchDate", dateStr);

            forwardToView("/WEB-INF/views/envoyer/list.jsp", request, response);
        } catch (ParseException e) {
            logger.log(Level.WARNING, "Format date invalide: " + dateStr, e);
            request.setAttribute("error", "Format de date invalide. Utilisez yyyy-MM-dd");
            listEnvois(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur recherche par date: " + dateStr, e);
            handleError(request, response, "Erreur lors de la recherche par date");
        }
    }

    private void handleAddEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Envoyer newEnvoi = extractEnvoiFromRequest(request);
            String idEnv = UUID.randomUUID().toString();
            newEnvoi.setIdEnv(idEnv);

            // Ajout du transfert
            boolean success = envoyerService.addEnvoi(newEnvoi);

            if (success) {
                // Stocker le message de succès en session
                request.getSession().setAttribute("successMessage",
                        "Transfert #" + idEnv + " effectué avec succès");

                // Redirection vers la liste des transferts
                response.sendRedirect(request.getContextPath() + "/envoyer");
            } else {
                throw new SQLException("Échec de l'ajout du transfert");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout", e);
            request.setAttribute("error", e.getMessage());
            showNewForm(request, response);
        }
    }

    private void handleUpdateEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Envoyer envoi = extractEnvoiFromRequest(request);
            String idEnv = request.getParameter("idEnv");

            if (idEnv == null || idEnv.isEmpty()) {
                throw new IllegalArgumentException("ID transfert manquant");
            }

            envoi.setIdEnv(idEnv);

            logger.log(Level.INFO, "Mise à jour transfert: {0}", envoi);

            envoyerService.updateEnvoi(envoi);

            request.getSession().setAttribute("successMessage",
                    "Transfert #" + idEnv + " modifié avec succès");

            // Recharger la liste mise à jour
            List<Envoyer> envois = envoyerService.getAllEnvois();
            request.setAttribute("envois", envois);

            forwardToView("/WEB-INF/views/envoyer/list.jsp", request, response);

        } catch (IllegalArgumentException | ParseException e) {
            logger.log(Level.WARNING, "Erreur validation formulaire", e);
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur mise à jour transfert", e);
            handleError(request, response, "Erreur lors de la modification du transfert");
        }
    }

    private void handleDeleteEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idEnv = request.getParameter("idEnv");
        if (idEnv == null || idEnv.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID transfert manquant");
            redirectToEnvoiList(response);
            return;
        }

        try {
            logger.log(Level.INFO, "Suppression transfert ID: {0}", idEnv);

            envoyerService.deleteEnvoi(idEnv);

            request.getSession().setAttribute("successMessage",
                    "Transfert #" + idEnv + " supprimé avec succès");

            // Recharger la liste mise à jour
            List<Envoyer> envois = envoyerService.getAllEnvois();
            request.setAttribute("envois", envois);

            forwardToView("/WEB-INF/views/envoyer/list.jsp", request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur suppression transfert ID: " + idEnv, e);
            request.getSession().setAttribute("errorMessage",
                    "Erreur lors de la suppression du transfert");
            redirectToEnvoiList(response);
        }
    }

    private void generatePdf(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String numtel = request.getParameter("numtel");
            int month = Integer.parseInt(request.getParameter("month"));
            int year = Integer.parseInt(request.getParameter("year"));

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Mois doit être entre 1 et 12");
            }

            Client client = clientService.getClientByNumtel(numtel);
            if (client == null) {
                throw new IllegalArgumentException("Client non trouvé");
            }

            List<Envoyer> operations = envoyerService.getEnvoisByClientAndMonth(numtel, month, year);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"releve_" + numtel + "_" + month + "_" + year + ".pdf\"");

            try (OutputStream out = response.getOutputStream()) {
                PDFGenerator.generateOperationStatement(client, operations, month, year, out);
            } catch (DocumentException e) {
                throw new ServletException("Erreur génération PDF", e);
            }
        } catch (NumberFormatException e) {
            throw new ServletException("Format mois/année invalide", e);
        } catch (SQLException e) {
            throw new ServletException("Erreur base de données", e);
        }
    }

    private Envoyer extractEnvoiFromRequest(HttpServletRequest request)
            throws ParseException, IllegalArgumentException {
        String numEnvoyeur = validateField(request, "numEnvoyeur", "Numéro envoyeur requis");
        String numRecepteur = validateField(request, "numRecepteur", "Numéro récepteur requis");
        String montantStr = validateField(request, "montant", "Montant requis");
        String dateStr = validateField(request, "date", "Date requise");

        if (numEnvoyeur.equals(numRecepteur)) {
            throw new IllegalArgumentException("L'envoyeur et le récepteur doivent être différents");
        }

        int montant = validatePositiveInt(montantStr, "Montant invalide ou négatif");
        Timestamp date = parseDateTime(dateStr);

        Envoyer envoi = new Envoyer();
        envoi.setNumEnvoyeur(numEnvoyeur);
        envoi.setNumRecepteur(numRecepteur);
        envoi.setMontant(montant);
        envoi.setDate(date);
        envoi.setRaison(request.getParameter("raison"));

        return envoi;
    }

    private String validateField(HttpServletRequest request, String fieldName, String errorMessage)
            throws IllegalArgumentException {
        String value = request.getParameter(fieldName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value.trim();
    }

    private int validatePositiveInt(String value, String errorMessage)
            throws IllegalArgumentException {
        try {
            int num = Integer.parseInt(value);
            if (num <= 0) throw new IllegalArgumentException(errorMessage);
            return num;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Timestamp parseDateTime(String dateStr) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            return new Timestamp(sdf.parse(dateStr).getTime());
        } catch (ParseException e) {
            throw new ParseException("Format de date invalide. Utilisez yyyy-MM-ddTHH:mm", 0);
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