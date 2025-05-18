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


import model.Patient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main application window for the medical management system.
 * @author wijde
 */
public class MainFrame extends JFrame {
    private JTextField searchField;
    private JTabbedPane tabbedPane;
    private HistoriquePanel historiquePanel;

    public MainFrame() {
        super("Système de Gestion Médicale");
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Main layout
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher Patient");
        searchPanel.add(new JLabel("Numéro Sécurité Sociale:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        historiquePanel = new HistoriquePanel();
        tabbedPane.addTab("Historique Consultations", historiquePanel);
        // Add more tabs as needed (e.g., Patient Details, Medications)

        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Search action
        searchButton.addActionListener((ActionEvent e) -> {
            String numSecu = searchField.getText().trim();
            if (!numSecu.isEmpty()) {
                // Simulate patient lookup (replace with real database query)
                Patient patient = new Patient("Doe", "John", java.time.LocalDate.of(1980, 1, 1), numSecu);
                historiquePanel.updatePatient(patient);
            } else {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Veuillez entrer un numéro de sécurité sociale",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);
            if (loginDialog.isAuthenticated()) {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}