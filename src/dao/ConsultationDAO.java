/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author wijde
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import model.Consultation;
import model.Medicament;
import model.Patient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Consultation entities.
 * @author wijde
 */
public class ConsultationDAO {
    private static final Logger LOGGER = Logger.getLogger(ConsultationDAO.class.getName());

    /**
     * Saves a new consultation and its prescriptions.
     * @param consultation The consultation to save
     * @return The ID of the saved consultation
     * @throws SQLException If a database error occurs
     */
    public int save(Consultation consultation) throws SQLException {
        String sql = "INSERT INTO consultations (patient_id, date_heure, medecin, specialite, diagnostic, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, consultation.getPatient().getId());
            stmt.setObject(2, consultation.getDateHeure());
            stmt.setString(3, consultation.getMedecin());
            stmt.setString(4, consultation.getSpecialite());
            stmt.setString(5, consultation.getDiagnostic());
            stmt.setString(6, consultation.getNotes());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int consultationId = rs.getInt(1);
                        savePrescriptions(conn, consultationId, consultation.getPrescriptions());
                        LOGGER.log(Level.INFO, "Consultation saved with ID: {0}", consultationId);
                        return consultationId;
                    }
                }
            }
            throw new SQLException("Failed to save consultation");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving consultation", e);
            throw e;
        }
    }

    /**
     * Finds all consultations for a patient.
     * @param patientId The patient ID
     * @return List of consultations
     * @throws SQLException If a database error occurs
     */
    public List<Consultation> findByPatientId(int patientId) throws SQLException {
        String sql = "SELECT c.id, c.date_heure, c.medecin, c.specialite, c.diagnostic, c.notes, " +
                     "p.id AS patient_id, p.nom, p.prenom, p.date_naissance, p.numero_securite_sociale " +
                     "FROM consultations c JOIN patients p ON c.patient_id = p.id " +
                     "WHERE c.patient_id = ?";
        List<Consultation> consultations = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("numero_securite_sociale")
                    );
                    patient.setId(rs.getInt("patient_id"));
                    Consultation consultation = new Consultation(patient, rs.getString("medecin"), rs.getString("specialite"));
                    consultation.setId(rs.getInt("id"));
                    consultation.setDateHeure(rs.getTimestamp("date_heure").toLocalDateTime());
                    consultation.setDiagnostic(rs.getString("diagnostic"));
                    consultation.setNotes(rs.getString("notes"));
                    consultation.setPrescriptions(getPrescriptions(conn, consultation.getId()));
                    consultations.add(consultation);
                }
            }
            LOGGER.log(Level.INFO, "Found {0} consultations for patient ID {1}", new Object[]{consultations.size(), patientId});
            return consultations;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding consultations for patient ID: " + patientId, e);
            throw e;
        }
    }

    /**
     * Updates an existing consultation.
     * @param consultation The consultation to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean update(Consultation consultation) throws SQLException {
        String sql = "UPDATE consultations SET date_heure = ?, medecin = ?, specialite = ?, " +
                     "diagnostic = ?, notes = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, consultation.getDateHeure());
            stmt.setString(2, consultation.getMedecin());
            stmt.setString(3, consultation.getSpecialite());
            stmt.setString(4, consultation.getDiagnostic());
            stmt.setString(5, consultation.getNotes());
            stmt.setInt(6, consultation.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update prescriptions
                deletePrescriptions(conn, consultation.getId());
                savePrescriptions(conn, consultation.getId(), consultation.getPrescriptions());
                LOGGER.log(Level.INFO, "Consultation updated with ID: {0}", consultation.getId());
                return true;
            }
            LOGGER.log(Level.WARNING, "No consultation found with ID: {0}", consultation.getId());
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating consultation ID: " + consultation.getId(), e);
            throw e;
        }
    }

    /**
     * Deletes a consultation.
     * @param consultationId The consultation ID
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException If a database error occurs
     */
    public boolean delete(int consultationId) throws SQLException {
        String sql = "DELETE FROM consultations WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Delete prescriptions first
            deletePrescriptions(conn, consultationId);
            stmt.setInt(1, consultationId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Consultation deleted with ID: {0}", consultationId);
                return true;
            }
            LOGGER.log(Level.WARNING, "No consultation found with ID: {0}", consultationId);
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting consultation ID: " + consultationId, e);
            throw e;
        }
    }

    /**
     * Saves prescriptions for a consultation.
     * @param conn The database connection
     * @param consultationId The consultation ID
     * @param prescriptions The list of medicaments
     * @throws SQLException If a database error occurs
     */
    private void savePrescriptions(Connection conn, int consultationId, List<Medicament> prescriptions) throws SQLException {
        String sql = "INSERT INTO consultation_medicaments (consultation_id, medicament_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Medicament med : prescriptions) {
                stmt.setInt(1, consultationId);
                stmt.setInt(2, med.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
            LOGGER.log(Level.INFO, "Saved {0} prescriptions for consultation ID: {1}", new Object[]{prescriptions.size(), consultationId});
        }
    }

    /**
     * Retrieves prescriptions for a consultation.
     * @param conn The database connection
     * @param consultationId The consultation ID
     * @return List of medicaments
     * @throws SQLException If a database error occurs
     */
    private List<Medicament> getPrescriptions(Connection conn, int consultationId) throws SQLException {
        String sql = "SELECT m.id, m.nom_commercial, m.principe_actif, m.dosage, m.forme, m.laboratoire, " +
                     "m.date_prescription, m.date_fin, m.posologie, m.indications, m.contre_indications " +
                     "FROM medicaments m JOIN consultation_medicaments cm ON m.id = cm.medicament_id " +
                     "WHERE cm.consultation_id = ?";
        List<Medicament> prescriptions = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consultationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Medicament med = new Medicament(
                        rs.getString("nom_commercial"),
                        rs.getString("principe_actif"),
                        rs.getString("dosage")
                    );
                    med.setId(rs.getInt("id"));
                    med.setForme(rs.getString("forme"));
                    med.setLaboratoire(rs.getString("laboratoire"));
                    med.setDatePrescription(rs.getDate("date_prescription") != null ?
                        rs.getDate("date_prescription").toLocalDate() : null);
                    med.setDateFin(rs.getDate("date_fin") != null ?
                        rs.getDate("date_fin").toLocalDate() : null);
                    med.setPosologie(rs.getString("posologie"));
                    med.setIndications(rs.getString("indications"));
                    med.setContreIndications(rs.getString("contre_indications"));
                    prescriptions.add(med);
                }
            }
            LOGGER.log(Level.INFO, "Retrieved {0} prescriptions for consultation ID: {1}", new Object[]{prescriptions.size(), consultationId});
            return prescriptions;
        }
    }

    /**
     * Deletes all prescriptions for a consultation.
     * @param conn The database connection
     * @param consultationId The consultation ID
     * @throws SQLException If a database error occurs
     */
    private void deletePrescriptions(Connection conn, int consultationId) throws SQLException {
        String sql = "DELETE FROM consultation_medicaments WHERE consultation_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consultationId);
            stmt.executeUpdate();
            LOGGER.log(Level.INFO, "Deleted prescriptions for consultation ID: {0}", consultationId);
        }
    }

    /**
     * Gets a database connection.
     * @return A Connection object
     * @throws SQLException If a connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        // Replace with your database connection logic
        String url = "jdbc:mysql://localhost:3306/medical_db"; // Example for MySQL
        String user = "wejden";
        String password = "admin123";
        return DriverManager.getConnection(url, user, password);
        // Alternatively, use a connection pool like HikariCP
    }
}
