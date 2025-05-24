package com.taptapsend.service;

import com.taptapsend.dao.ClientDAO;
import com.taptapsend.dao.EnvoyerDAO;
import com.taptapsend.dao.FraisEnvoiDAO;
import com.taptapsend.dao.TauxDAO;
import com.taptapsend.model.Client;
import com.taptapsend.model.Envoyer;
import com.taptapsend.model.FraisEnvoi;
import com.taptapsend.model.Taux;
import com.taptapsend.util.EmailUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EnvoyerService {
    private final EnvoyerDAO envoyerDAO = new EnvoyerDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final TauxDAO tauxDAO = new TauxDAO();
    private final FraisEnvoiDAO fraisEnvoiDAO = new FraisEnvoiDAO();
    private final EmailUtil emailUtil = new EmailUtil();

    public void addEnvoi(Envoyer envoi) throws SQLException {
        // Vérifier que l'envoi est vers un pays étranger
        Client envoyeur = clientDAO.getClientByNumtel(envoi.getNumEnvoyeur());
        if (envoyeur == null) {
            throw new SQLException("Client envoyeur introuvable");
        }

        Client recepteur = clientDAO.getClientByNumtel(envoi.getNumRecepteur());
        if (recepteur == null) {
            throw new SQLException("Client récepteur introuvable");
        }

        if (envoyeur.getPays().equals(recepteur.getPays())) {
            throw new SQLException("L'envoi doit être vers un pays étranger");
        }

        // Calculer le frais d'envoi
        String idfrais = envoyeur.getPays() + "-" + recepteur.getPays();
        FraisEnvoi fraisEnvoi = fraisEnvoiDAO.getFraisEnvoiById(idfrais);
        if (fraisEnvoi == null) {
            throw new SQLException("Aucun frais d'envoi configuré pour cette paire de pays");
        }

        int frais = fraisEnvoi.getFrais();

        // Vérifier le solde de l'envoyeur
        int nouveauSoldeEnvoyeur = envoyeur.getSolde() - envoi.getMontant() - frais;
        if (nouveauSoldeEnvoyeur < 0) {
            throw new SQLException("Solde insuffisant pour effectuer l'envoi");
        }

        // Calculer le montant reçu avec le taux de change
        String idtaux = envoyeur.getPays() + "-" + recepteur.getPays();
        Taux taux = tauxDAO.getTauxById(idtaux);
        if (taux == null) {
            throw new SQLException("Aucun taux de change configuré pour cette paire de pays");
        }

        int montantRecu = (envoi.getMontant() * taux.getMontant2()) / taux.getMontant1();

        // Mettre à jour les soldes
        clientDAO.updateClientSolde(envoyeur.getNumtel(), nouveauSoldeEnvoyeur);
        clientDAO.updateClientSolde(recepteur.getNumtel(), recepteur.getSolde() + montantRecu);

        // Générer un ID unique pour l'envoi
        envoi.setIdEnv(UUID.randomUUID().toString());
        envoi.setDate(new Timestamp(System.currentTimeMillis()));

        // Enregistrer l'envoi
        envoyerDAO.addEnvoi(envoi);

        // Envoyer les emails de notification
        emailUtil.sendTransferEmail(envoyeur, recepteur, envoi.getMontant(), montantRecu, frais);
    }

    // Les autres méthodes restent inchangées
    public Envoyer getEnvoiById(String idEnv) throws SQLException {
        return envoyerDAO.getEnvoiById(idEnv)
                .orElseThrow(() -> new SQLException("Aucun envoi trouvé avec l'ID: " + idEnv));
    }

    public List<Envoyer> getAllEnvois() throws SQLException {
        return envoyerDAO.getAllEnvois();
    }

    public List<Envoyer> getEnvoisByDate(Timestamp date) throws SQLException {
        return envoyerDAO.getEnvoisByDate(date);
    }

    public List<Envoyer> getEnvoisByClientAndMonth(String numtel, int month, int year) throws SQLException {
        return envoyerDAO.getEnvoisByClientAndMonth(numtel, month, year);
    }

    public void updateEnvoi(Envoyer envoi) throws SQLException {
        if (!envoyerDAO.getEnvoiById(envoi.getIdEnv()).isPresent()) {
            throw new SQLException("Envoi non trouvé, mise à jour impossible");
        }
        envoyerDAO.updateEnvoi(envoi);
    }

    public void deleteEnvoi(String idEnv) throws SQLException {
        if (!envoyerDAO.getEnvoiById(idEnv).isPresent()) {
            throw new SQLException("Envoi non trouvé, suppression impossible");
        }
        envoyerDAO.deleteEnvoi(idEnv);
    }

    public int getTotalFrais() throws SQLException {
        return envoyerDAO.getTotalFrais();
    }
}