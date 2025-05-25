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
        urlPatterns = {"/dashboard", "/"},  // Ajout du pattern racine
        loadOnStartup = 1
)
public class DashboardController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    private EnvoyerService envoyerService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.envoyerService = new EnvoyerService();
            LOGGER.info("DashboardController initialisé avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Échec de l'initialisation du DashboardController", e);
            throw new ServletException("Échec de l'initialisation: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        try {
            // Debug: Afficher les informations de chemin
            LOGGER.info("Requête reçue - Contexte: " + request.getContextPath()
                    + ", ServletPath: " + request.getServletPath()
                    + ", PathInfo: " + request.getPathInfo());

            // Préparation des données pour la vue
            prepareDashboardData(request);

            // Affichage de la vue
            displayView(request, response, "/WEB-INF/views/dashboard.jsp");

            LOGGER.info("Dashboard affiché avec succès en "
                    + (System.currentTimeMillis() - startTime) + "ms");

        } catch (SQLException e) {
            handleDatabaseError(request, response, e);
        } catch (Exception e) {
            handleGenericError(response, e);
        }
    }

    private void prepareDashboardData(HttpServletRequest request) throws SQLException {
        int totalFrais = envoyerService.getTotalFrais();
        request.setAttribute("totalFrais", totalFrais);
        request.setAttribute("pageTitle", "Tableau de bord");

        // Vous pouvez ajouter d'autres données ici si nécessaire
        // request.setAttribute("autresDonnees", envoyerService.getAutresDonnees());
    }

    private void displayView(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        if (getServletContext().getResource(viewPath) == null) {
            LOGGER.severe("Vue introuvable: " + viewPath);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ressource introuvable");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response, SQLException e)
            throws ServletException, IOException {
        LOGGER.log(Level.SEVERE, "Erreur SQL: " + e.getMessage(), e);
        request.setAttribute("errorMessage", "Erreur de base de données: " + e.getMessage());
        displayView(request, response, "/WEB-INF/views/error.jsp");
    }

    private void handleGenericError(HttpServletResponse response, Exception e) throws IOException {
        LOGGER.log(Level.SEVERE, "Erreur inattendue: " + e.getMessage(), e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue: " + e.getMessage());
    }
}