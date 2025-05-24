package com.taptapsend.service;

import com.taptapsend.dao.TauxDAO;
import com.taptapsend.model.Taux;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TauxService {
    private static final Logger LOGGER = Logger.getLogger(TauxService.class.getName());
    private final TauxDAO tauxDAO;

    // Injection de dépendance via constructeur
    public TauxService() {
        this.tauxDAO = new TauxDAO();
    }

    // Pour les tests unitaires
    public TauxService(TauxDAO tauxDAO) {
        this.tauxDAO = tauxDAO;
    }

    public void addTaux(Taux taux) throws SQLException {
        try {
            validateTaux(taux);
            tauxDAO.addTaux(taux);
            LOGGER.log(Level.INFO, "Taux ajouté avec succès: {0}", taux.getIdtaux());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du taux: " + taux, e);
            throw new SQLException("Erreur lors de l'ajout du taux", e);
        }
    }

    public Taux getTauxById(String idtaux) throws SQLException {
        try {
            if (idtaux == null || idtaux.trim().isEmpty()) {
                throw new IllegalArgumentException("L'id du taux ne peut pas être vide");
            }
            return tauxDAO.getTauxById(idtaux);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du taux ID: " + idtaux, e);
            throw new SQLException("Erreur lors de la récupération du taux", e);
        }
    }

    public List<Taux> getAllTaux() {
        try {
            return tauxDAO.getAllTaux();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les taux", e);
            return Collections.emptyList(); // Retourne une liste vide au lieu de propager l'exception
        }
    }

    public void updateTaux(Taux taux) throws SQLException {
        try {
            validateTaux(taux);
            tauxDAO.updateTaux(taux);
            LOGGER.log(Level.INFO, "Taux mis à jour avec succès: {0}", taux.getIdtaux());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du taux: " + taux, e);
            throw new SQLException("Erreur lors de la mise à jour du taux", e);
        }
    }

    public void deleteTaux(String idtaux) throws SQLException {
        try {
            if (idtaux == null || idtaux.trim().isEmpty()) {
                throw new IllegalArgumentException("L'id du taux ne peut pas être vide");
            }
            tauxDAO.deleteTaux(idtaux);
            LOGGER.log(Level.INFO, "Taux supprimé avec succès: {0}", idtaux);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du taux ID: " + idtaux, e);
            throw new SQLException("Erreur lors de la suppression du taux", e);
        }
    }

    private void validateTaux(Taux taux) throws IllegalArgumentException {
        if (taux == null) {
            throw new IllegalArgumentException("Le taux ne peut pas être null");
        }
        if (taux.getIdtaux() == null || taux.getIdtaux().trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du taux est requis");
        }
        if (!taux.getIdtaux().matches("^[A-Z]{3}-[A-Z]{3}$")) {
            throw new IllegalArgumentException("Format ID taux invalide. Doit être AAA-BBB (ex: EUR-USD)");
        }
        if (taux.getMontant1() <= 0 || taux.getMontant2() <= 0) {
            throw new IllegalArgumentException("Les montants doivent être positifs");
        }
    }
}