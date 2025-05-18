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
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Dialog for user authentication.
 * @author wijde
 */
public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private boolean authenticated = false;

    public LoginDialog(Frame parent) {
        super(parent, "Connexion", true);
        initComponents();
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    private void initComponents() {
        // Layout
        setLayout(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Components
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Connexion");
        cancelButton = new JButton("Annuler");

        // Add components to panels
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add padding
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Action listeners
        loginButton.addActionListener((ActionEvent e) -> {
            // Simple authentication check (replace with real authentication logic)
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("password")) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginDialog.this,
                        "Nom d'utilisateur ou mot de passe incorrect",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}