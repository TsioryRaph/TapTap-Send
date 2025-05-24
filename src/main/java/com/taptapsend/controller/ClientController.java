package com.taptapsend.controller;

import com.taptapsend.model.Client;
import com.taptapsend.service.ClientService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/client")
public class ClientController extends HttpServlet {
    private final ClientService clientService = new ClientService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action != null) {
                switch (action) {
                    case "add":
                        addClient(request, response);
                        break;
                    case "update":
                        updateClient(request, response);
                        break;
                    case "delete":
                        deleteClient(request, response);
                        break;
                    default:
                        response.sendRedirect("client");
                        break;
                }
            } else {
                response.sendRedirect("client");
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur SQL: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new ServletException("Format de nombre invalide pour le solde", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                listClients(request, response);
            } else {
                switch (action) {
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "add":  // Nouveau cas pour afficher le formulaire d'ajout
                        showAddForm(request, response);
                        break;
                    case "search":
                        searchClients(request, response);
                        break;
                    default:
                        listClients(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur SQL: " + e.getMessage(), e);
        }
    }

    private void listClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients();
        request.setAttribute("clients", clients);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/client/list.jsp");
        dispatcher.forward(request, response);
    }

    private void searchClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Client> clients = clientService.searchClients(searchTerm);
        request.setAttribute("clients", clients);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/client/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String numtel = request.getParameter("numtel");
        if (numtel != null && !numtel.isEmpty()) {
            Client client = clientService.getClientByNumtel(numtel);
            request.setAttribute("client", client);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/client/edit.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("client");
        }
    }

    // Nouvelle méthode pour afficher le formulaire d'ajout
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/client/add.jsp");
        dispatcher.forward(request, response);
    }

    private void addClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            String numtel = request.getParameter("numtel");
            String nom = request.getParameter("nom");
            String sexe = request.getParameter("sexe");
            String pays = request.getParameter("pays");
            int solde = Integer.parseInt(request.getParameter("solde"));
            String mail = request.getParameter("mail");

            // Validation des champs obligatoires
            if (numtel == null || numtel.isEmpty() || nom == null || nom.isEmpty()) {
                throw new IllegalArgumentException("Les champs obligatoires ne sont pas remplis");
            }

            Client newClient = new Client(numtel, nom, sexe, pays, solde, mail);
            clientService.addClient(newClient);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le solde doit être un nombre valide");
        } finally {
            response.sendRedirect("client");
        }
    }

    private void updateClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String numtel = request.getParameter("numtel");
        String nom = request.getParameter("nom");
        String sexe = request.getParameter("sexe");
        String pays = request.getParameter("pays");
        int solde = Integer.parseInt(request.getParameter("solde"));
        String mail = request.getParameter("mail");

        Client client = new Client(numtel, nom, sexe, pays, solde, mail);
        clientService.updateClient(client);
        response.sendRedirect("client");
    }

    private void deleteClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String numtel = request.getParameter("numtel");
        if (numtel != null && !numtel.isEmpty()) {
            clientService.deleteClient(numtel);
        }
        response.sendRedirect("client");
    }
}