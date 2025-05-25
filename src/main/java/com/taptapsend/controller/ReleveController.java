package com.taptapsend.controller;

import com.taptapsend.dao.ClientDAO;
import com.taptapsend.service.ReleveService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ReleveController", urlPatterns = {"/releve"})
public class ReleveController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReleveController.class.getName());
    private ReleveService releveService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialisation avec le ClientDAO requis
        ClientDAO clientDAO = new ClientDAO(); // Assurez-vous que ClientDAO est correctement implémenté
        this.releveService = new ReleveService(clientDAO);
        logger.info("ReleveController initialisé");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Vérification de l'authentification
            if (request.getSession().getAttribute("user") == null) {
                logger.warning("Tentative d'accès non autorisée à /releve");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Ajout d'attributs utiles pour la JSP
            request.setAttribute("clients", releveService.getClientsActifs());
            request.setAttribute("pageTitle", "Génération de relevé");

            // Forward vers la vue
            request.getRequestDispatcher("/WEB-INF/views/releve.jsp").forward(request, response);
            logger.info("Affichage du formulaire de relevé");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans doGet de ReleveController", e);
            request.setAttribute("error", "Erreur lors du chargement de la page");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Récupération des paramètres
            String numClient = request.getParameter("numClient");
            String periode = request.getParameter("periode");
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");

            logger.info("Tentative de génération de relevé pour client: " + numClient);

            // Validation des paramètres
            if (numClient == null || numClient.isEmpty()) {
                throw new IllegalArgumentException("Le numéro client est requis");
            }

            // Génération du PDF
            byte[] pdfContent = releveService.genererRelevePDF(numClient, periode, dateDebut, dateFin);

            // Envoi du PDF en réponse
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"releve_" + numClient + ".pdf\"");
            response.setContentLength(pdfContent.length);
            response.getOutputStream().write(pdfContent);
            logger.info("Relevé généré avec succès");

        } catch (IllegalArgumentException e) {
            logger.warning("Erreur de validation: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la génération du PDF", e);
            request.setAttribute("error", "Erreur lors de la génération du relevé");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}