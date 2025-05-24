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

@WebServlet(name = "DashboardController",
        urlPatterns = {"/TapTapSend/dashboard", "/dashboard"},
        loadOnStartup = 1)
public class DashboardController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    private EnvoyerService envoyerService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.envoyerService = new EnvoyerService();
        LOGGER.info("DashboardController initialisé");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.info("Requête reçue pour le dashboard");
        LOGGER.info("URL demandée: " + request.getRequestURL());
        LOGGER.info("Chemin contexte: " + request.getContextPath());

        try {
            int totalFrais = envoyerService.getTotalFrais();
            request.setAttribute("totalFrais", totalFrais);
            request.setAttribute("pageTitle", "Tableau de bord");

            String jspPath = "/WEB-INF/views/dashboard.jsp";
            LOGGER.info("Tentative de forward vers: " + jspPath);

            // Vérification de l'existence du fichier JSP
            if (getServletContext().getResource(jspPath) == null) {
                LOGGER.severe("Fichier JSP introuvable: " + jspPath);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ressource non trouvée");
                return;
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
            dispatcher.forward(request, response);
            LOGGER.info("Forward réussi vers le dashboard");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur SQL", e);
            request.setAttribute("errorMessage", "Erreur de base de données");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inattendue", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erreur interne du serveur");
        }
    }
}