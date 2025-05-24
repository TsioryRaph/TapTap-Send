package com.taptapsend.service;

import com.taptapsend.dao.ClientDAO;
import com.taptapsend.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO;

    // Injection du DAO via le constructeur pour meilleure testabilité
    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    // Constructeur par défaut conservé pour compatibilité
    public ClientService() {
        this(new ClientDAO());
    }

    public void addClient(Client client) throws SQLException {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }
        clientDAO.addClient(client);
    }

    public Client getClientByNumtel(String numtel) throws SQLException {
        if (numtel == null || numtel.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être vide");
        }
        return clientDAO.getClientByNumtel(numtel);
    }

    public List<Client> getAllClients() throws SQLException {
        return clientDAO.getAllClients();
    }

    public List<Client> searchClients(String searchTerm) throws SQLException {
        if (searchTerm == null) {
            throw new IllegalArgumentException("Le terme de recherche ne peut pas être null");
        }
        return clientDAO.searchClients(searchTerm.trim());
    }

    public void updateClient(Client client) throws SQLException {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }
        clientDAO.updateClient(client);
    }

    public void deleteClient(String numtel) throws SQLException {
        if (numtel == null || numtel.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être vide");
        }
        clientDAO.deleteClient(numtel);
    }

    public void updateSoldeEnvoyeur(String numtelEnvoyeur, int montant) throws SQLException {
        if (numtelEnvoyeur == null || numtelEnvoyeur.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone de l'envoyeur ne peut pas être vide");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        Client envoyeur = clientDAO.getClientByNumtel(numtelEnvoyeur);
        if (envoyeur == null) {
            throw new SQLException("Client envoyeur non trouvé");
        }
        if (envoyeur.getSolde() < montant) {
            throw new IllegalStateException("Solde insuffisant");
        }

        int nouveauSolde = envoyeur.getSolde() - montant;
        clientDAO.updateClientSolde(numtelEnvoyeur, nouveauSolde);
    }

    public void updateSoldeRecepteur(String numtelRecepteur, int montant) throws SQLException {
        if (numtelRecepteur == null || numtelRecepteur.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone du récepteur ne peut pas être vide");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        Client recepteur = clientDAO.getClientByNumtel(numtelRecepteur);
        if (recepteur == null) {
            throw new SQLException("Client récepteur non trouvé");
        }

        int nouveauSolde = recepteur.getSolde() + montant;
        clientDAO.updateClientSolde(numtelRecepteur, nouveauSolde);
    }

    public void updateClientSolde(String numtel, int newSolde) throws SQLException {
        if (numtel == null || numtel.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être vide");
        }
        clientDAO.updateClientSolde(numtel, newSolde);
    }

    // Nouvelle méthode pour gérer un transfert complet
    public void transfererArgent(String numtelEnvoyeur, String numtelRecepteur, int montant) throws SQLException {
        try {
            // Démarrer une transaction (si votre DAO le supporte)
            // clientDAO.beginTransaction();

            updateSoldeEnvoyeur(numtelEnvoyeur, montant);
            updateSoldeRecepteur(numtelRecepteur, montant);

            // Valider la transaction
            // clientDAO.commitTransaction();
        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            // clientDAO.rollbackTransaction();
            throw e;
        }
    }
}