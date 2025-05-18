/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author wijde
 */



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a consultation in the system.
 * @author wijde
 */
public class Consultation {
    private int id;
    private Patient patient;
    private LocalDateTime dateHeure;
    private String medecin;
    private String specialite;
    private String diagnostic;
    private String notes;
    private final List<Medicament> prescriptions = new ArrayList<>();

    // Constructeurs
    public Consultation() {
        this.dateHeure = LocalDateTime.now();
    }

    public Consultation(Patient patient, String medecin, String specialite) {
        this();
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        if (medecin == null || medecin.trim().isEmpty()) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null ou vide");
        }
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être null ou vide");
        }
        this.patient = patient;
        this.medecin = medecin.trim();
        this.specialite = specialite.trim();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        this.patient = patient;
    }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) {
        if (dateHeure == null) {
            throw new IllegalArgumentException("La date et l'heure ne peuvent pas être null");
        }
        this.dateHeure = dateHeure;
    }

    public String getMedecin() { return medecin; }
    public void setMedecin(String medecin) {
        if (medecin == null || medecin.trim().isEmpty()) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null ou vide");
        }
        this.medecin = medecin.trim();
    }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) {
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être null ou vide");
        }
        this.specialite = specialite.trim();
    }

    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic != null ? diagnostic.trim() : null;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) {
        this.notes = notes != null ? notes.trim() : null;
    }

    public List<Medicament> getPrescriptions() { return new ArrayList<>(prescriptions); }
    public void addPrescription(Medicament medicament) {
        if (medicament == null) {
            throw new IllegalArgumentException("Le médicament ne peut pas être null");
        }
        this.prescriptions.add(medicament);
    }

    public void setPrescriptions(List<Medicament> prescriptions) {
        if (prescriptions == null) {
            throw new IllegalArgumentException("La liste des prescriptions ne peut pas être null");
        }
        this.prescriptions.clear();
        for (Medicament med : prescriptions) {
            if (med != null) {
                this.prescriptions.add(med);
            }
        }
    }

    // Méthodes métier
    public String getResume() {
        String diagSummary = (diagnostic != null && !diagnostic.isEmpty()) ? 
            diagnostic.substring(0, Math.min(20, diagnostic.length())) + (diagnostic.length() > 20 ? "..." : "") : 
            "N/A";
        return String.format("[%s] %s - %s (%s)", 
            dateHeure.toLocalDate(),
            medecin,
            specialite,
            diagSummary);
    }

    @Override
    public String toString() {
        return getResume();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consultation that = (Consultation) o;
        return id == that.id && dateHeure.equals(that.dateHeure) && patient.equals(that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateHeure, patient);
    }
}