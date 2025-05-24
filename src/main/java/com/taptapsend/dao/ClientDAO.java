package com.taptapsend.dao;

import com.taptapsend.model.Client;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClientDAO {
    private final QueryRunner queryRunner = new QueryRunner();

    public void addClient(Client client) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "INSERT INTO client (numtel, nom, sexe, pays, solde, mail) VALUES (?, ?, ?, ?, ?, ?)";
        queryRunner.update(connection, sql,
                client.getNumtel(), client.getNom(), client.getSexe(),
                client.getPays(), client.getSolde(), client.getMail());
    }

    public Client getClientByNumtel(String numtel) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM client WHERE numtel = ?";
        ResultSetHandler<Client> handler = new BeanHandler<>(Client.class);
        return queryRunner.query(connection, sql, handler, numtel);
    }

    public List<Client> getAllClients() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM client";
        ResultSetHandler<List<Client>> handler = new BeanListHandler<>(Client.class);
        return queryRunner.query(connection, sql, handler);
    }

    public List<Client> searchClients(String searchTerm) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM client WHERE nom LIKE ? OR numtel LIKE ?";
        ResultSetHandler<List<Client>> handler = new BeanListHandler<>(Client.class);
        return queryRunner.query(connection, sql, handler,
                "%" + searchTerm + "%", "%" + searchTerm + "%");
    }

    public void updateClient(Client client) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "UPDATE client SET nom = ?, sexe = ?, pays = ?, solde = ?, mail = ? WHERE numtel = ?";
        queryRunner.update(connection, sql,
                client.getNom(), client.getSexe(), client.getPays(),
                client.getSolde(), client.getMail(), client.getNumtel());
    }

    public void deleteClient(String numtel) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "DELETE FROM client WHERE numtel = ?";
        queryRunner.update(connection, sql, numtel);
    }

    public void updateClientSolde(String numtel, int newSolde) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "UPDATE client SET solde = ? WHERE numtel = ?";
        queryRunner.update(connection, sql, newSolde, numtel);
    }
}