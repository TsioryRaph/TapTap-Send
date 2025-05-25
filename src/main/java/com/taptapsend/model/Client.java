package com.taptapsend.model;

import java.util.Objects;

public class Client {
    private String numtel;
    private String nom;
    private String sexe;
    private String pays;
    private int solde;
    private String mail;

    public Client() {
        // Constructeur par défaut
    }

    public Client(String numtel, String nom, String sexe, String pays, int solde, String mail) {
        this.numtel = Objects.requireNonNull(numtel, "Le numéro de téléphone ne peut pas être null");
        this.nom = Objects.requireNonNull(nom, "Le nom ne peut pas être null");
        this.sexe = sexe; // Peut être null selon votre modèle
        this.pays = Objects.requireNonNull(pays, "Le pays ne peut pas être null");
        this.solde = solde;
        this.mail = mail; // Peut être null selon votre modèle
    }

    // Getters and Setters
    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = Objects.requireNonNull(numtel, "Le numéro de téléphone ne peut pas être null");
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = Objects.requireNonNull(nom, "Le nom ne peut pas être null");
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = Objects.requireNonNull(pays, "Le pays ne peut pas être null");
    }

    public int getSolde() {
        return solde;
    }

    public void setSolde(int solde) {
        this.solde = solde;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // Méthodes utilitaires
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return numtel.equals(client.numtel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numtel);
    }

    @Override
    public String toString() {
        return "Client{" +
                "numtel='" + numtel + '\'' +
                ", nom='" + nom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", pays='" + pays + '\'' +
                ", solde=" + solde +
                ", mail='" + mail + '\'' +
                '}';
    }
}