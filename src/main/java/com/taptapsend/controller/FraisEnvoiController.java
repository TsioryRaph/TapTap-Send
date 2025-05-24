package com.taptapsend.controller;

import com.taptapsend.model.FraisEnvoi;
import com.taptapsend.service.FraisEnvoiService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/frais")
public class FraisEnvoiController extends HttpServlet {
    private final FraisEnvoiService fraisEnvoiService = new FraisEnvoiService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "add":
                    addFraisEnvoi(request, response);
                    break;
                case "update":
                    updateFraisEnvoi(request, response);
                    break;
                case "delete":
                    deleteFraisEnvoi(request, response);
                    break;
                default:
                    listFraisEnvoi(request, response);
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
                listFraisEnvoi(request, response);
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
                        listFraisEnvoi(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listFraisEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<FraisEnvoi> fraisEnvoiList = fraisEnvoiService.getAllFraisEnvoi();
        request.setAttribute("fraisEnvoiList", fraisEnvoiList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/frais/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idfrais = request.getParameter("idfrais");
        FraisEnvoi fraisEnvoi = fraisEnvoiService.getFraisEnvoiById(idfrais);
        request.setAttribute("fraisEnvoi", fraisEnvoi);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/frais/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/frais/add.jsp");
        dispatcher.forward(request, response);
    }

    private void showDeleteConfirmation(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idfrais = request.getParameter("idfrais");
        FraisEnvoi fraisEnvoi = fraisEnvoiService.getFraisEnvoiById(idfrais);
        request.setAttribute("fraisEnvoi", fraisEnvoi);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/frais/delete.jsp");
        dispatcher.forward(request, response);
    }

    private void addFraisEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idfrais = request.getParameter("idfrais");
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));
        int frais = Integer.parseInt(request.getParameter("frais"));

        FraisEnvoi newFraisEnvoi = new FraisEnvoi(idfrais, montant1, montant2, frais);
        fraisEnvoiService.addFraisEnvoi(newFraisEnvoi);
        response.sendRedirect(request.getContextPath() + "/frais");
    }

    private void updateFraisEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idfrais = request.getParameter("idfrais");
        int montant1 = Integer.parseInt(request.getParameter("montant1"));
        int montant2 = Integer.parseInt(request.getParameter("montant2"));
        int frais = Integer.parseInt(request.getParameter("frais"));

        FraisEnvoi fraisEnvoi = new FraisEnvoi(idfrais, montant1, montant2, frais);
        fraisEnvoiService.updateFraisEnvoi(fraisEnvoi);
        response.sendRedirect(request.getContextPath() + "/frais");
    }

    private void deleteFraisEnvoi(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idfrais = request.getParameter("idfrais");
        fraisEnvoiService.deleteFraisEnvoi(idfrais);
        response.sendRedirect(request.getContextPath() + "/frais");
    }
}