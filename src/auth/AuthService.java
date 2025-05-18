/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

/**
 *
 * @author wijde
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author wijde
 */



import dao.UserDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for handling user authentication.
 * @author wijde
 */
public class AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private static volatile AuthService instance;
    private final UserDAO userDAO;
    private User currentUser;

    // Private constructor for singleton
    private AuthService() {
        this.userDAO = new UserDAO(); // Ideally, inject this dependency
    }

    /**
     * Gets the singleton instance of AuthService.
     * @return The singleton instance
     */
    public static AuthService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new AuthService();
                }
            }
        }
        return instance;
    }

    /**
     * Authenticates a user.
     * @param username The username
     * @param password The plaintext password
     * @return true if authentication is successful
     * @throws IllegalArgumentException if username or password is null or empty
     */
    public boolean authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur et le mot de passe ne peuvent pas être null ou vides");
        }
        try {
            User user = userDAO.findByUsername(username.trim());
            if (user != null && PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
                this.currentUser = user;
                LOGGER.log(Level.INFO, "Utilisateur authentifi\u00e9: {0}", username);
                return true;
            }
            LOGGER.log(Level.WARNING, "\u00c9chec de l''authentification pour: {0}", username);
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur d''authentification pour {0}: {1}", new Object[]{username, e.getMessage()});
            throw new RuntimeException("Erreur d'accès à la base de données", e);
        }
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        if (currentUser != null) {
            LOGGER.log(Level.INFO, "D\u00e9connexion de l''utilisateur: {0}", currentUser.getUsername());
            this.currentUser = null;
        }
    }

    /**
     * Gets the currently authenticated user.
     * @return The current user or null if no user is authenticated
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Checks if a user is authenticated.
     * @return true if a user is authenticated
     */
    public boolean isAuthenticated() {
        return this.currentUser != null;
    }

    /**
     * Changes the password of the current user.
     * @param currentPassword The current password
     * @param newPassword The new password
     * @return true if the password change was successful
     * @throws IllegalArgumentException if passwords are null or empty
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        if (!isAuthenticated()) {
            LOGGER.warning("Tentative de changement de mot de passe sans utilisateur authentifié");
            return false;
        }
        if (currentPassword == null || currentPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Les mots de passe ne peuvent pas être null ou vides");
        }
        try {
            User user = currentUser;
            if (PasswordUtils.verifyPassword(currentPassword, user.getPasswordHash())) {
                String newHash = PasswordUtils.hashPassword(newPassword);
                boolean success = userDAO.updatePassword(user.getId(), newHash);
                if (success) {
                    user.setPasswordHash(newHash);
                    LOGGER.log(Level.INFO, "Mot de passe chang\u00e9 pour l''utilisateur: {0}", user.getUsername());
                } else {
                    LOGGER.log(Level.WARNING, "\u00c9chec de la mise \u00e0 jour du mot de passe pour: {0}", user.getUsername());
                }
                return success;
            }
            LOGGER.log(Level.WARNING, "Mot de passe actuel incorrect pour: {0}", user.getUsername());
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du changement de mot de passe pour {0}: {1}", new Object[]{currentUser.getUsername(), e.getMessage()});
            throw new RuntimeException("Erreur d'accès à la base de données", e);
        }
    }
}