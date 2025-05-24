package com.taptapsend.service;

import com.taptapsend.dao.ClientDAO;
import com.taptapsend.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAO();

    public void addClient(Client client) throws SQLException {
        clientDAO.addClient(client);
    }

    public Client getClientByNumtel(String numtel) throws SQLException {
        return clientDAO.getClientByNumtel(numtel);
    }

    public List<Client> getAllClients() throws SQLException {
        return clientDAO.getAllClients();
    }

    public List<Client> searchClients(String searchTerm) throws SQLException {
        return clientDAO.searchClients(searchTerm);
    }

    public void updateClient(Client client) throws SQLException {
        clientDAO.updateClient(client);
    }

    public void deleteClient(String numtel) throws SQLException {
        clientDAO.deleteClient(numtel);
    }

    public void updateClientSolde(String numtel, int newSolde) throws SQLException {
        clientDAO.updateClientSolde(numtel, newSolde);
    }
}