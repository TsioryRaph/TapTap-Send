package com.taptapsend.dao;

import com.taptapsend.model.Envoyer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class EnvoyerDAO {
    private final QueryRunner queryRunner = new QueryRunner();

    // Constantes pour les requêtes SQL
    private static final String INSERT_ENVOI_SQL =
            "INSERT INTO envoyer (idEnv, numEnvoyeur, numRecepteur, montant, date, raison) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ENVOI_BY_ID_SQL =
            "SELECT e.*, c.nom as nomRecepteur FROM envoyer e JOIN client c ON e.numRecepteur = c.numtel WHERE idEnv = ?";
    private static final String SELECT_ALL_ENVOIS_SQL =
            "SELECT e.*, c.nom as nomRecepteur FROM envoyer e JOIN client c ON e.numRecepteur = c.numtel";
    private static final String SELECT_ENVOIS_BY_DATE_SQL =
            "SELECT e.*, c.nom as nomRecepteur FROM envoyer e JOIN client c ON e.numRecepteur = c.numtel WHERE DATE(date) = DATE(?)";
    private static final String SELECT_ENVOIS_BY_CLIENT_MONTH_SQL =
            "SELECT e.*, c.nom as nomRecepteur FROM envoyer e JOIN client c ON e.numRecepteur = c.numtel " +
                    "WHERE numEnvoyeur = ? AND EXTRACT(MONTH FROM date) = ? AND EXTRACT(YEAR FROM date) = ?";
    private static final String UPDATE_ENVOI_SQL =
            "UPDATE envoyer SET numEnvoyeur = ?, numRecepteur = ?, montant = ?, date = ?, raison = ? WHERE idEnv = ?";
    private static final String DELETE_ENVOI_SQL =
            "DELETE FROM envoyer WHERE idEnv = ?";
    private static final String SELECT_TOTAL_FRAIS_SQL =
            "SELECT COALESCE(SUM(f.frais), 0) FROM envoyer e JOIN frais_envoi f ON " +
                    "CONCAT((SELECT pays FROM client WHERE numtel = e.numEnvoyeur), '-', " +
                    "(SELECT pays FROM client WHERE numtel = e.numRecepteur)) = f.idfrais";

    public void addEnvoi(Envoyer envoi) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            queryRunner.update(connection, INSERT_ENVOI_SQL,
                    envoi.getIdEnv(),
                    envoi.getNumEnvoyeur(),
                    envoi.getNumRecepteur(),
                    envoi.getMontant(),
                    envoi.getDate(),
                    envoi.getRaison());
        }
    }

    public Optional<Envoyer> getEnvoiById(String idEnv) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            ResultSetHandler<Envoyer> handler = new BeanHandler<>(Envoyer.class);
            Envoyer envoi = queryRunner.query(connection, SELECT_ENVOI_BY_ID_SQL, handler, idEnv);
            return Optional.ofNullable(envoi);
        }
    }

    public List<Envoyer> getAllEnvois() throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            ResultSetHandler<List<Envoyer>> handler = new BeanListHandler<>(Envoyer.class);
            return queryRunner.query(connection, SELECT_ALL_ENVOIS_SQL, handler);
        }
    }

    public List<Envoyer> getEnvoisByDate(Timestamp date) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            ResultSetHandler<List<Envoyer>> handler = new BeanListHandler<>(Envoyer.class);
            return queryRunner.query(connection, SELECT_ENVOIS_BY_DATE_SQL, handler, date);
        }
    }

    public List<Envoyer> getEnvoisByClientAndMonth(String numtel, int month, int year) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            ResultSetHandler<List<Envoyer>> handler = new BeanListHandler<>(Envoyer.class);
            return queryRunner.query(connection, SELECT_ENVOIS_BY_CLIENT_MONTH_SQL,
                    handler, numtel, month, year);
        }
    }

    public void updateEnvoi(Envoyer envoi) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            queryRunner.update(connection, UPDATE_ENVOI_SQL,
                    envoi.getNumEnvoyeur(),
                    envoi.getNumRecepteur(),
                    envoi.getMontant(),
                    envoi.getDate(),
                    envoi.getRaison(),
                    envoi.getIdEnv());
        }
    }

    public void deleteEnvoi(String idEnv) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            queryRunner.update(connection, DELETE_ENVOI_SQL, idEnv);
        }
    }

    public int getTotalFrais() throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            return queryRunner.query(connection, SELECT_TOTAL_FRAIS_SQL, new ScalarHandler<>());
        }
    }
}