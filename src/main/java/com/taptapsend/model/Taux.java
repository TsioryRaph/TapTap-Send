package com.taptapsend.model;

public class Taux {
    private String idtaux;
    private int montant1;
    private int montant2;

    public Taux() {}

    public Taux(String idtaux, int montant1, int montant2) {
        this.idtaux = idtaux;
        this.montant1 = montant1;
        this.montant2 = montant2;
    }

    // Getters and Setters
    public String getIdtaux() { return idtaux; }
    public void setIdtaux(String idtaux) { this.idtaux = idtaux; }
    public int getMontant1() { return montant1; }
    public void setMontant1(int montant1) { this.montant1 = montant1; }
    public int getMontant2() { return montant2; }
    public void setMontant2(int montant2) { this.montant2 = montant2; }
}