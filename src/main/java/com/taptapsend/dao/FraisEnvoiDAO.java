package com.taptapsend.dao;

import com.taptapsend.model.FraisEnvoi;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FraisEnvoiDAO {
    private final QueryRunner queryRunner = new QueryRunner();

    public void addFraisEnvoi(FraisEnvoi fraisEnvoi) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "INSERT INTO frais_envoi (idfrais, montant1, montant2, frais) VALUES (?, ?, ?, ?)";
        queryRunner.update(connection, sql,
                fraisEnvoi.getIdfrais(), fraisEnvoi.getMontant1(),
                fraisEnvoi.getMontant2(), fraisEnvoi.getFrais());
    }

    public FraisEnvoi getFraisEnvoiById(String idfrais) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM frais_envoi WHERE idfrais = ?";
        ResultSetHandler<FraisEnvoi> handler = new BeanHandler<>(FraisEnvoi.class);
        return queryRunner.query(connection, sql, handler, idfrais);
    }

    public List<FraisEnvoi> getAllFraisEnvoi() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM frais_envoi";
        ResultSetHandler<List<FraisEnvoi>> handler = new BeanListHandler<>(FraisEnvoi.class);
        return queryRunner.query(connection, sql, handler);
    }

    public void updateFraisEnvoi(FraisEnvoi fraisEnvoi) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "UPDATE frais_envoi SET montant1 = ?, montant2 = ?, frais = ? WHERE idfrais = ?";
        queryRunner.update(connection, sql,
                fraisEnvoi.getMontant1(), fraisEnvoi.getMontant2(),
                fraisEnvoi.getFrais(), fraisEnvoi.getIdfrais());
    }

    public void deleteFraisEnvoi(String idfrais) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "DELETE FROM frais_envoi WHERE idfrais = ?";
        queryRunner.update(connection, sql, idfrais);
    }
}