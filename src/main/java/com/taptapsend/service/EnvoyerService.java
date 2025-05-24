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
import java.util.Objects;
import java.util.UUID;

public class EnvoyerService {
    private final EnvoyerDAO envoyerDAO;
    private final ClientDAO clientDAO;
    private final TauxDAO tauxDAO;
    private final FraisEnvoiDAO fraisEnvoiDAO;
    private final EmailUtil emailUtil;

    // Injection des dépendances via le constructeur
    public EnvoyerService(EnvoyerDAO envoyerDAO, ClientDAO clientDAO,
                          TauxDAO tauxDAO, FraisEnvoiDAO fraisEnvoiDAO,
                          EmailUtil emailUtil) {
        this.envoyerDAO = Objects.requireNonNull(envoyerDAO);
        this.clientDAO = Objects.requireNonNull(clientDAO);
        this.tauxDAO = Objects.requireNonNull(tauxDAO);
        this.fraisEnvoiDAO = Objects.requireNonNull(fraisEnvoiDAO);
        this.emailUtil = Objects.requireNonNull(emailUtil);
    }

    // Constructeur par défaut pour compatibilité
    public EnvoyerService() {
        this(new EnvoyerDAO(), new ClientDAO(),
                new TauxDAO(), new FraisEnvoiDAO(),
                new EmailUtil());
    }

    public void addEnvoi(Envoyer envoi) throws SQLException {
        validateEnvoi(envoi);

        Client envoyeur = getClientOrThrow(envoi.getNumEnvoyeur(), "Client envoyeur introuvable");
        Client recepteur = getClientOrThrow(envoi.getNumRecepteur(), "Client récepteur introuvable");

        validateInternationalTransfer(envoyeur, recepteur);

        FraisEnvoi fraisEnvoi = getFraisEnvoiOrThrow(envoyeur.getPays(), recepteur.getPays());
        Taux taux = getTauxOrThrow(envoyeur.getPays(), recepteur.getPays());

        int montantTotal = envoi.getMontant() + fraisEnvoi.getFrais();
        validateSolde(envoyeur, montantTotal);

        int montantRecu = calculateMontantRecu(envoi.getMontant(), taux);

        processTransfer(envoi, envoyeur, recepteur, montantTotal, montantRecu, fraisEnvoi.getFrais());
    }

    private void validateEnvoi(Envoyer envoi) throws SQLException {
        if (envoi == null) {
            throw new SQLException("L'objet envoi ne peut pas être null");
        }
        if (envoi.getMontant() <= 0) {
            throw new SQLException("Le montant doit être positif");
        }
    }

    private Client getClientOrThrow(String numtel, String errorMessage) throws SQLException {
        Client client = clientDAO.getClientByNumtel(numtel);
        if (client == null) {
            throw new SQLException(errorMessage);
        }
        return client;
    }

    private void validateInternationalTransfer(Client envoyeur, Client recepteur) throws SQLException {
        if (envoyeur.getPays().equals(recepteur.getPays())) {
            throw new SQLException("L'envoi doit être vers un pays étranger");
        }
    }

    private FraisEnvoi getFraisEnvoiOrThrow(String paysEnvoyeur, String paysRecepteur) throws SQLException {
        String idfrais = paysEnvoyeur + "-" + paysRecepteur;
        FraisEnvoi fraisEnvoi = fraisEnvoiDAO.getFraisEnvoiById(idfrais);
        if (fraisEnvoi == null) {
            throw new SQLException("Aucun frais d'envoi configuré pour cette paire de pays");
        }
        return fraisEnvoi;
    }

    private Taux getTauxOrThrow(String paysEnvoyeur, String paysRecepteur) throws SQLException {
        String idtaux = paysEnvoyeur + "-" + paysRecepteur;
        Taux taux = tauxDAO.getTauxById(idtaux);
        if (taux == null) {
            throw new SQLException("Aucun taux de change configuré pour cette paire de pays");
        }
        return taux;
    }

    private void validateSolde(Client client, int montantTotal) throws SQLException {
        if (client.getSolde() < montantTotal) {
            throw new SQLException("Solde insuffisant pour effectuer l'envoi");
        }
    }

    private int calculateMontantRecu(int montant, Taux taux) {
        return (montant * taux.getMontant2()) / taux.getMontant1();
    }

    private void processTransfer(Envoyer envoi, Client envoyeur, Client recepteur,
                                 int montantTotal, int montantRecu, int frais) throws SQLException {
        try {
            // Mettre à jour les soldes
            clientDAO.updateClientSolde(envoyeur.getNumtel(), envoyeur.getSolde() - montantTotal);
            clientDAO.updateClientSolde(recepteur.getNumtel(), recepteur.getSolde() + montantRecu);

            // Enregistrer l'envoi avec les nouveaux champs
            envoi.setIdEnv(UUID.randomUUID().toString());
            envoi.setDate(new Timestamp(System.currentTimeMillis()));
            envoi.setFrais(frais);  // Utilisation du setter
            envoi.setMontantRecu(montantRecu);  // Utilisation du setter

            envoyerDAO.addEnvoi(envoi);

            // Envoyer les notifications
            emailUtil.sendTransferEmail(envoyeur, recepteur, envoi.getMontant(), montantRecu, frais);

        } catch (SQLException e) {
            throw new SQLException("Erreur lors du traitement du transfert: " + e.getMessage(), e);
        }
    }

    public Envoyer getEnvoiById(String idEnv) throws SQLException {
        return envoyerDAO.getEnvoiById(idEnv)
                .orElseThrow(() -> new SQLException("Aucun envoi trouvé avec l'ID: " + idEnv));
    }

    public List<Envoyer> getAllEnvois() throws SQLException {
        return envoyerDAO.getAllEnvois();
    }

    public List<Envoyer> getEnvoisByDate(Timestamp date) throws SQLException {
        if (date == null) {
            throw new SQLException("La date ne peut pas être null");
        }
        return envoyerDAO.getEnvoisByDate(date);
    }

    public List<Envoyer> getEnvoisByClientAndMonth(String numtel, int month, int year) throws SQLException {
        if (numtel == null || numtel.trim().isEmpty()) {
            throw new SQLException("Le numéro de téléphone ne peut pas être vide");
        }
        if (month < 1 || month > 12) {
            throw new SQLException("Le mois doit être entre 1 et 12");
        }
        return envoyerDAO.getEnvoisByClientAndMonth(numtel, month, year);
    }

    public void updateEnvoi(Envoyer envoi) throws SQLException {
        if (envoi == null) {
            throw new SQLException("L'objet envoi ne peut pas être null");
        }
        if (!envoyerDAO.getEnvoiById(envoi.getIdEnv()).isPresent()) {
            throw new SQLException("Envoi non trouvé, mise à jour impossible");
        }
        envoyerDAO.updateEnvoi(envoi);
    }

    public void deleteEnvoi(String idEnv) throws SQLException {
        if (idEnv == null || idEnv.trim().isEmpty()) {
            throw new SQLException("L'ID de l'envoi ne peut pas être vide");
        }
        if (!envoyerDAO.getEnvoiById(idEnv).isPresent()) {
            throw new SQLException("Envoi non trouvé, suppression impossible");
        }
        envoyerDAO.deleteEnvoi(idEnv);
    }

    public int getTotalFrais() throws SQLException {
        return envoyerDAO.getTotalFrais();
    }
}