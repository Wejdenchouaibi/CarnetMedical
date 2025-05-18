/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */


import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a medicament in the system.
 * @author wijde
 */
public class Medicament {
    private int id;
    private String nomCommercial;
    private String principeActif;
    private String dosage;
    private String forme;
    private String laboratoire;
    private LocalDate datePrescription;
    private LocalDate dateFin;
    private String posologie;
    private String indications;
    private String contreIndications;

    // Constructeurs
    public Medicament() {}

    public Medicament(String nomCommercial, String principeActif, String dosage) {
        if (nomCommercial == null || nomCommercial.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom commercial ne peut pas être null ou vide");
        }
        if (principeActif == null || principeActif.trim().isEmpty()) {
            throw new IllegalArgumentException("Le principe actif ne peut pas être null ou vide");
        }
        if (dosage == null || dosage.trim().isEmpty()) {
            throw new IllegalArgumentException("Le dosage ne peut pas être null ou vide");
        }
        this.nomCommercial = nomCommercial.trim();
        this.principeActif = principeActif.trim();
        this.dosage = dosage.trim();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomCommercial() { return nomCommercial; }
    public void setNomCommercial(String nomCommercial) {
        if (nomCommercial == null || nomCommercial.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom commercial ne peut pas être null ou vide");
        }
        this.nomCommercial = nomCommercial.trim();
    }

    public String getPrincipeActif() { return principeActif; }
    public void setPrincipeActif(String principeActif) {
        if (principeActif == null || principeActif.trim().isEmpty()) {
            throw new IllegalArgumentException("Le principe actif ne peut pas être null ou vide");
        }
        this.principeActif = principeActif.trim();
    }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) {
        if (dosage == null || dosage.trim().isEmpty()) {
            throw new IllegalArgumentException("Le dosage ne peut pas être null ou vide");
        }
        this.dosage = dosage.trim();
    }

    public String getForme() { return forme; }
    public void setForme(String forme) {
        this.forme = forme != null ? forme.trim() : null;
    }

    public String getLaboratoire() { return laboratoire; }
    public void setLaboratoire(String laboratoire) {
        this.laboratoire = laboratoire != null ? laboratoire.trim() : null;
    }

    public LocalDate getDatePrescription() { return datePrescription; }
    public void setDatePrescription(LocalDate datePrescription) {
        this.datePrescription = datePrescription;
    }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getPosologie() { return posologie; }
    public void setPosologie(String posologie) {
        this.posologie = posologie != null ? posologie.trim() : null;
    }

    public String getIndications() { return indications; }
    public void setIndications(String indications) {
        this.indications = indications != null ? indications.trim() : null;
    }

    public String getContreIndications() { return contreIndications; }
    public void setContreIndications(String contreIndications) {
        this.contreIndications = contreIndications != null ? contreIndications.trim() : null;
    }

    // Méthodes métier
    /**
     * Checks if the medicament is currently active.
     * @return true if the medicament is active based on prescription and end dates
     */
    public boolean estEnCours() {
        LocalDate today = LocalDate.now();
        boolean afterPrescription = datePrescription == null || !datePrescription.isAfter(today);
        boolean beforeEnd = dateFin == null || !dateFin.isBefore(today);
        return afterPrescription && beforeEnd;
    }

    /**
     * Gets a complete description of the medicament.
     * @return A formatted string with medicament details
     */
    public String getDescriptionComplete() {
        return String.format("%s (%s - %s) - %s - Posologie: %s",
            nomCommercial,
            principeActif,
            dosage,
            forme != null ? forme : "Forme non spécifiée",
            posologie != null ? posologie : "Non spécifiée");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicament that = (Medicament) o;
        return Objects.equals(nomCommercial, that.nomCommercial) &&
               Objects.equals(principeActif, that.principeActif) &&
               Objects.equals(dosage, that.dosage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomCommercial, principeActif, dosage);
    }

    @Override
    public String toString() {
        return getDescriptionComplete();
    }
}