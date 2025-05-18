/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import java.util.Objects;

/**
 *
 * @author wijde
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 * Represents a user in the system.
 * @author wijde
 */


/**
 * Represents a user in the system.
 * @author wijde
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;

    // Constructor
    public User() {
        this.id = -1; // Valeur par défaut pour indiquer un ID non défini
        this.username = "";
        this.passwordHash = "";
    }

    public User(int id, String username, String passwordHash) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être null ou vide");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Le hash du mot de passe ne peut pas être null ou vide");
        }
        this.id = id;
        this.username = username.trim();
        this.passwordHash = passwordHash.trim();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être null ou vide");
        }
        this.username = username.trim();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Le hash du mot de passe ne peut pas être null ou vide");
        }
        this.passwordHash = passwordHash.trim();
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}