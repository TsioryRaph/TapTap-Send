package com.taptapsend.model;

public class FraisEnvoi {
    private String idfrais;
    private int montant1;
    private int montant2;
    private int frais;

    public FraisEnvoi() {}

    public FraisEnvoi(String idfrais, int montant1, int montant2, int frais) {
        this.idfrais = idfrais;
        this.montant1 = montant1;
        this.montant2 = montant2;
        this.frais = frais;
    }

    // Getters and Setters
    public String getIdfrais() { return idfrais; }
    public void setIdfrais(String idfrais) { this.idfrais = idfrais; }
    public int getMontant1() { return montant1; }
    public void setMontant1(int montant1) { this.montant1 = montant1; }
    public int getMontant2() { return montant2; }
    public void setMontant2(int montant2) { this.montant2 = montant2; }
    public int getFrais() { return frais; }
    public void setFrais(int frais) { this.frais = frais; }
}