package com.taptapsend.model;

import java.sql.Timestamp;

public class Envoyer {
    private String idEnv;
    private String numEnvoyeur;
    private String numRecepteur;
    private int montant;
    private int montantRecu;    // Montant effectivement reçu après conversion
    private int frais;          // Frais de transfert appliqués
    private Timestamp date;
    private String raison;
    private String nomRecepteur; // Pour l'affichage seulement

    public Envoyer() {}

    // Constructeur complet avec tous les champs
    public Envoyer(String idEnv, String numEnvoyeur, String numRecepteur,
                   int montant, int montantRecu, int frais,
                   Timestamp date, String raison) {
        this.idEnv = idEnv;
        this.numEnvoyeur = numEnvoyeur;
        this.numRecepteur = numRecepteur;
        this.montant = montant;
        this.montantRecu = montantRecu;
        this.frais = frais;
        this.date = date;
        this.raison = raison;
    }

    // Getters and Setters
    public String getIdEnv() { return idEnv; }
    public void setIdEnv(String idEnv) { this.idEnv = idEnv; }

    public String getNumEnvoyeur() { return numEnvoyeur; }
    public void setNumEnvoyeur(String numEnvoyeur) { this.numEnvoyeur = numEnvoyeur; }

    public String getNumRecepteur() { return numRecepteur; }
    public void setNumRecepteur(String numRecepteur) { this.numRecepteur = numRecepteur; }

    public int getMontant() { return montant; }
    public void setMontant(int montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        this.montant = montant;
    }

    public int getMontantRecu() { return montantRecu; }
    public void setMontantRecu(int montantRecu) {
        if (montantRecu <= 0) {
            throw new IllegalArgumentException("Le montant reçu doit être positif");
        }
        this.montantRecu = montantRecu;
    }

    public int getFrais() { return frais; }
    public void setFrais(int frais) {
        if (frais < 0) {
            throw new IllegalArgumentException("Les frais ne peuvent pas être négatifs");
        }
        this.frais = frais;
    }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être null");
        }
        this.date = date;
    }

    public String getRaison() { return raison; }
    public void setRaison(String raison) { this.raison = raison; }

    public String getNomRecepteur() { return nomRecepteur; }
    public void setNomRecepteur(String nomRecepteur) { this.nomRecepteur = nomRecepteur; }

    @Override
    public String toString() {
        return "Envoyer{" +
                "idEnv='" + idEnv + '\'' +
                ", numEnvoyeur='" + numEnvoyeur + '\'' +
                ", numRecepteur='" + numRecepteur + '\'' +
                ", montant=" + montant +
                ", montantRecu=" + montantRecu +
                ", frais=" + frais +
                ", date=" + date +
                ", raison='" + raison + '\'' +
                '}';
    }
}