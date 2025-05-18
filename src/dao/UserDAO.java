/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;




import auth.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for User entities.
 * @author wijde
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private static final String DB_URL = "jdbc:mysql://localhost:3306/medical_db";
    private static final String DB_USER = "wejden";
    private static final String DB_PASSWORD = "admin123";

    /**
     * Finds a user by their username.
     * @param username The username to search for
     * @return The User object if found, null otherwise
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If username is null or empty
     */
    public User findByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être null ou vide");
        }
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                    );
                    LOGGER.log(Level.INFO, "Utilisateur trouvé: {0}", username);
                    return user;
                }
                LOGGER.log(Level.INFO, "Aucun utilisateur trouvé pour: {0}", username);
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de l'utilisateur: " + username, e);
            throw e;
        }
    }

    /**
     * Updates the password hash for a user.
     * @param userId The ID of the user
     * @param newPasswordHash The new password hash
     * @return true if the update was successful, false otherwise
     * @throws SQLException If a database error occurs
     * @throws IllegalArgumentException If userId is invalid or newPasswordHash is null/empty
     */
    public boolean updatePassword(int userId, String newPasswordHash) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("L'ID de l'utilisateur doit être positif");
        }
        if (newPasswordHash == null || newPasswordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Le hash du mot de passe ne peut pas être null ou vide");
        }
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newPasswordHash.trim());
                stmt.setInt(2, userId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Commit transaction
                    LOGGER.log(Level.INFO, "Mot de passe mis à jour pour l'utilisateur ID: {0}", userId);
                    return true;
                }
                LOGGER.log(Level.WARNING, "Aucun utilisateur trouvé avec l'ID: {0}", userId);
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", rollbackEx);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du mot de passe pour l'utilisateur ID: " + userId, e);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion", closeEx);
                }
            }
        }
    }

    /**
     * Gets a database connection.
     * @return A Connection object
     * @throws SQLException If a connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        // TODO: Use a connection pool (e.g., HikariCP) in production
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
