package com.taptapsend.service;

import com.taptapsend.dao.FraisEnvoiDAO;
import com.taptapsend.model.FraisEnvoi;

import java.sql.SQLException;
import java.util.List;

public class FraisEnvoiService {
    private final FraisEnvoiDAO fraisEnvoiDAO = new FraisEnvoiDAO();

    public void addFraisEnvoi(FraisEnvoi fraisEnvoi) throws SQLException {
        fraisEnvoiDAO.addFraisEnvoi(fraisEnvoi);
    }

    public FraisEnvoi getFraisEnvoiById(String idfrais) throws SQLException {
        return fraisEnvoiDAO.getFraisEnvoiById(idfrais);
    }

    public List<FraisEnvoi> getAllFraisEnvoi() throws SQLException {
        return fraisEnvoiDAO.getAllFraisEnvoi();
    }

    public void updateFraisEnvoi(FraisEnvoi fraisEnvoi) throws SQLException {
        fraisEnvoiDAO.updateFraisEnvoi(fraisEnvoi);
    }

    public void deleteFraisEnvoi(String idfrais) throws SQLException {
        fraisEnvoiDAO.deleteFraisEnvoi(idfrais);
    }
}