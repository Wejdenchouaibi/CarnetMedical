/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author wijde
package model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the system.
 * @author wijde
 */
public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String groupeSanguin;
    private String numeroSecuriteSociale;
    private final List<Consultation> consultations = new ArrayList<>();
    private final List<Medicament> medicaments = new ArrayList<>();

    // Constructeurs
    public Patient() {
        this.nom = "";
        this.prenom = "";
        this.numeroSecuriteSociale = "";
    }

    public Patient(String nom, String prenom, LocalDate dateNaissance, String numeroSecuriteSociale) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être null ou vide");
        }
        if (dateNaissance == null) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être null");
        }
        if (numeroSecuriteSociale == null || numeroSecuriteSociale.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de sécurité sociale ne peut pas être null ou vide");
        }
        this.nom = nom.trim();
        this.prenom = prenom.trim();
        this.dateNaissance = dateNaissance;
        this.numeroSecuriteSociale = numeroSecuriteSociale.trim();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide");
        }
        this.nom = nom.trim();
    }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être null ou vide");
        }
        this.prenom = prenom.trim();
    }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) {
        if (dateNaissance == null) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être null");
        }
        this.dateNaissance = dateNaissance;
    }

    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin != null ? groupeSanguin.trim() : null;
    }

    public String getNumeroSecuriteSociale() { return numeroSecuriteSociale; }
    public void setNumeroSecuriteSociale(String numeroSecuriteSociale) {
        if (numeroSecuriteSociale == null || numeroSecuriteSociale.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de sécurité sociale ne peut pas être null ou vide");
        }
        this.numeroSecuriteSociale = numeroSecuriteSociale.trim();
    }

    public List<Consultation> getConsultations() { return new ArrayList<>(consultations); }
    public void addConsultation(Consultation consultation) {
        if (consultation == null) {
            throw new IllegalArgumentException("La consultation ne peut pas être null");
        }
        consultation.setPatient(this);
        this.consultations.add(consultation);
    }

    public List<Medicament> getMedicaments() { return new ArrayList<>(medicaments); }
    public void addMedicament(Medicament medicament) {
        if (medicament == null) {
            throw new IllegalArgumentException("Le médicament ne peut pas être null");
        }
        this.medicaments.add(medicament);
    }

    // Méthodes utilitaires
    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + numeroSecuriteSociale + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id == patient.id && numeroSecuriteSociale.equals(patient.numeroSecuriteSociale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroSecuriteSociale);
    }
}