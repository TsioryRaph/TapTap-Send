package com.taptapsend.controller;

import com.taptapsend.service.EnvoyerService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(
        name = "DashboardController",
        urlPatterns = {"/dashboard"},
        loadOnStartup = 1
)
public class DashboardController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    private EnvoyerService envoyerService;

    @Override
    public void init() throws ServletException {
        try {
            // Initialisation avec vérification rigoureuse
            this.envoyerService = new EnvoyerService();

            // Vérification ABSOLUE du fichier JSP
            String jspPath = "/WEB-INF/views/dashboard.jsp";
            String realPath = getServletContext().getRealPath(jspPath);

            LOGGER.info("Vérification du JSP à l'emplacement: " + realPath);

            if (realPath == null) {
                throw new ServletException("Le chemin du JSP est null - Vérifiez la structure du projet");
            }

            if (!new java.io.File(realPath).exists()) {
                throw new ServletException("Fichier JSP manquant. Doit être à: " + realPath);
            }

            LOGGER.info("DashboardController initialisé avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "ÉCHEC CRITIQUE LORS DE L'INITIALISATION", e);
            throw new ServletException("Échec de l'initialisation du contrôleur", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Journalisation complète
            LOGGER.info(String.format(
                    "Requête reçue - URL: %s | IP: %s | Session: %s",
                    request.getRequestURL(),
                    request.getRemoteAddr(),
                    request.getSession().getId()
            ));

            // 1. Préparation des données - Modification ici : utilisation de Long
            Long totalFrais = envoyerService.getTotalFrais();
            LOGGER.info("Données récupérées - TotalFrais: " + totalFrais);

            request.setAttribute("totalFrais", totalFrais);
            request.setAttribute("pageTitle", "Tableau de bord");

            // 2. Forward vers la vue avec double vérification
            String viewPath = "/WEB-INF/views/dashboard.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);

            if (dispatcher == null) {
                throw new ServletException("Dispatcher non trouvé pour: " + viewPath);
            }

            // Dernière vérification avant forward
            if (response.isCommitted()) {
                LOGGER.warning("La réponse a déjà été commitée!");
                return;
            }

            dispatcher.forward(request, response);
            LOGGER.info("Affichage du dashboard réussi");

        } catch (Exception e) {
            handleGenericError(request, response, e);
        }
    }

    private void handleGenericError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws IOException, ServletException {
        LOGGER.log(Level.SEVERE, "ERREUR NON GÉRÉE", e);

        if (e instanceof SQLException) {
            // Gestion spécifique des erreurs SQL
            LOGGER.log(Level.SEVERE, "ERREUR DE BASE DE DONNÉES", e);
            request.setAttribute("errorMessage", "Problème de connexion à la base de données");
            request.setAttribute("errorDetails", e.getMessage());

            try {
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            } catch (IllegalStateException ex) {
                LOGGER.severe("Erreur lors du forward vers error.jsp: " + ex.getMessage());
                if (!response.isCommitted()) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            // Gestion des autres erreurs
            if (!response.isCommitted()) {
                try {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Une erreur technique est survenue");
                } catch (IllegalStateException ex) {
                    LOGGER.severe("Échec de l'envoi de l'erreur: " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("Nettoyage du DashboardController");
        // Libérer les ressources si nécessaire
    }
}