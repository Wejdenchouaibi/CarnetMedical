/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author wijde
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nibh://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import model.Consultation;
import model.Patient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel displaying a patient's consultation history.
 * @author wijde
 */
public class HistoriquePanel extends JPanel {
    private JTable consultationTable;
    private DefaultTableModel tableModel;
    private Patient currentPatient;

    public HistoriquePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Table model
        String[] columns = {"Date", "Médecin", "Spécialité", "Diagnostic"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        consultationTable = new JTable(tableModel);
        consultationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(consultationTable);
        add(scrollPane, BorderLayout.CENTER);

        // Double-click to view details
        consultationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = consultationTable.getSelectedRow();
                    if (row >= 0 && currentPatient != null) {
                        Consultation consultation = currentPatient.getConsultations().get(row);
                        showConsultationDetails(consultation);
                    }
                }
            }
        });
    }

    public void updatePatient(Patient patient) {
        this.currentPatient = patient;
        tableModel.setRowCount(0); // Clear table
        if (patient != null) {
            for (Consultation consultation : patient.getConsultations()) {
                tableModel.addRow(new Object[]{
                    consultation.getDateHeure().toLocalDate(),
                    consultation.getMedecin(),
                    consultation.getSpecialite(),
                    consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "N/A"
                });
            }
        }
    }

    private void showConsultationDetails(Consultation consultation) {
        StringBuilder details = new StringBuilder();
        details.append("Date: ").append(consultation.getDateHeure()).append("\n");
        details.append("Médecin: ").append(consultation.getMedecin()).append("\n");
        details.append("Spécialité: ").append(consultation.getSpecialite()).append("\n");
        details.append("Diagnostic: ").append(consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "N/A").append("\n");
        details.append("Notes: ").append(consultation.getNotes() != null ? consultation.getNotes() : "Aucune").append("\n");
        details.append("Prescriptions:\n");
        for (model.Medicament med : consultation.getPrescriptions()) {
            details.append("- ").append(med.getDescriptionComplete()).append("\n");
        }

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Détails de la Consultation", JOptionPane.INFORMATION_MESSAGE);
    }
}