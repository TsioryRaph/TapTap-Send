package com.taptapsend.controller;

import com.taptapsend.model.Taux;
import com.taptapsend.service.TauxService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/taux")
public class TauxController extends HttpServlet {
    private final TauxService tauxService = new TauxService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addTaux(request, response);
                    break;
                case "update":
                    updateTaux(request, response);
                    break;
                case "delete":
                    deleteTaux(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/taux");
                    break;
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur de base de données: " + e.getMessage());
            doGet(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Format de nombre invalide");
            doGet(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                listTaux(request, response);
            } else {
                switch (action) {
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "add":
                        showAddForm(request, response);
                        break;
                    case "delete":
                        showDeleteConfirmation(request, response);
                        break;
                    default:
                        listTaux(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listTaux(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Taux> tauxList = tauxService.getAllTaux();
        request.setAttribute("tauxList", tauxList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/taux/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idtaux = request.getParameter("idtaux");
        Taux taux = tauxService.getTauxById(idtaux);
        request.setAttribute("taux", taux);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/taux/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/taux/add.jsp");
        dispatcher.forward(request, response);
    }

    private void showDeleteConfirmation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idtaux = request.getParameter("idtaux");
        Taux taux = tauxService.getTauxById(idtaux);
        request.setAttribute("taux", taux);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/taux/delete.jsp");
        dispatcher.forward(request, response);
    }

    private void addTaux(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            String idtaux = validateIdTaux(request.getParameter("idtaux"));
            int montant1 = validateMontant(request.getParameter("montant1"));
            int montant2 = validateMontant(request.getParameter("montant2"));

            Taux newTaux = new Taux(idtaux, montant1, montant2);
            tauxService.addTaux(newTaux);
            response.sendRedirect(request.getContextPath() + "/taux?success=Le+taux+a+été+ajouté+avec+succès");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showAddForm(request, response);
        }
    }

    private void updateTaux(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            String idtaux = validateIdTaux(request.getParameter("idtaux"));
            int montant1 = validateMontant(request.getParameter("montant1"));
            int montant2 = validateMontant(request.getParameter("montant2"));

            Taux taux = new Taux(idtaux, montant1, montant2);
            tauxService.updateTaux(taux);
            response.sendRedirect(request.getContextPath() + "/taux?success=Le+taux+a+été+modifié+avec+succès");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteTaux(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idtaux = request.getParameter("idtaux");
        tauxService.deleteTaux(idtaux);
        response.sendRedirect(request.getContextPath() + "/taux?success=Le+taux+a+été+supprimé+avec+succès");
    }

    // Méthodes de validation
    private String validateIdTaux(String idtaux) {
        if (idtaux == null || !idtaux.matches("^[A-Z]{3}-[A-Z]{3}$")) {
            throw new IllegalArgumentException("Format ID taux invalide. Doit être comme: EUR-MGA");
        }
        return idtaux;
    }

    private int validateMontant(String montantStr) {
        try {
            int montant = Integer.parseInt(montantStr);
            if (montant <= 0) {
                throw new IllegalArgumentException("Les montants doivent être positifs");
            }
            return montant;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format de nombre invalide pour le montant");
        }
    }
}