package com.taptapsend.dao;

import com.taptapsend.model.Taux;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TauxDAO {
    private final QueryRunner queryRunner = new QueryRunner();

    public void addTaux(Taux taux) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "INSERT INTO taux (idtaux, montant1, montant2) VALUES (?, ?, ?)";
        queryRunner.update(connection, sql, taux.getIdtaux(), taux.getMontant1(), taux.getMontant2());
    }

    public Taux getTauxById(String idtaux) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM taux WHERE idtaux = ?";
        ResultSetHandler<Taux> handler = new BeanHandler<>(Taux.class);
        return queryRunner.query(connection, sql, handler, idtaux);
    }

    public List<Taux> getAllTaux() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM taux";
        ResultSetHandler<List<Taux>> handler = new BeanListHandler<>(Taux.class);
        return queryRunner.query(connection, sql, handler);
    }

    public void updateTaux(Taux taux) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "UPDATE taux SET montant1 = ?, montant2 = ? WHERE idtaux = ?";
        queryRunner.update(connection, sql, taux.getMontant1(), taux.getMontant2(), taux.getIdtaux());
    }

    public void deleteTaux(String idtaux) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String sql = "DELETE FROM taux WHERE idtaux = ?";
        queryRunner.update(connection, sql, idtaux);
    }
}