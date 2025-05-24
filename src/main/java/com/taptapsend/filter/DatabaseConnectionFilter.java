package com.taptapsend.filter;

import com.taptapsend.dao.DBConnection;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter("/*")
public class DatabaseConnectionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("DatabaseConnectionFilter initialized - PostgreSQL connection OK");
        } catch (SQLException e) {
            System.err.println("ERREUR: Impossible de se connecter à PostgreSQL");
            e.printStackTrace();
            throw new ServletException("Échec de la connexion PostgreSQL. Vérifiez:\n"
                    + "1. Que PostgreSQL est démarré\n"
                    + "2. Que la BD 'taptapsend_db' existe\n"
                    + "3. Que l'utilisateur/mot de passe sont corrects", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Liste des chemins à exclure du filtre
        if (path.startsWith("/resources/") ||
                path.startsWith("/WEB-INF/") ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".png") ||
                path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            // Définir un timeout pour éviter les connexions bloquantes
            connection.setNetworkTimeout(
                    java.util.concurrent.Executors.newSingleThreadExecutor(),
                    5000); // 5 secondes timeout

            request.setAttribute("dbConnection", connection);
            chain.doFilter(request, response);
        } catch (SQLException e) {
            // Journalisation plus détaillée
            System.err.println("ERREUR DB pour la requête: " + httpRequest.getRequestURL());
            e.printStackTrace();

            // Rediriger vers une page d'erreur
            request.setAttribute("errorMessage", "Problème de connexion à la base de données");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("DatabaseConnectionFilter détruit");
    }
}