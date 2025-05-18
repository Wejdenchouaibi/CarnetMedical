/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

/**
 *
 * @author wijde
 */



import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtils {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int SALT_LENGTH = 16;

    /**
     * Hashe un mot de passe avec un sel aléatoire.
     * @param password Mot de passe en clair
     * @return String encodé: algorithme:itérations:tailleClé:sel:hash
     * @throws IllegalArgumentException si le mot de passe est null ou vide
     * @throws RuntimeException en cas d'erreur de hachage
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
        }
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), 
                salt, 
                ITERATIONS, 
                KEY_LENGTH
            );

            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return String.format("%s:%d:%d:%s:%s",
                ALGORITHM,
                ITERATIONS,
                KEY_LENGTH,
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hash)
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }

    /**
     * Vérifie un mot de passe contre un hash stocké.
     * @param password Mot de passe en clair à vérifier
     * @param storedHash Hash stocké au format algorithme:itérations:tailleClé:sel:hash
     * @return true si le mot de passe correspond
     * @throws IllegalArgumentException si le mot de passe ou le hash est null ou vide
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || password.isEmpty() || storedHash == null || storedHash.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe et le hash ne peuvent pas être null ou vides");
        }
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 5) return false;

            String algorithm = parts[0];
            int iterations = Integer.parseInt(parts[1]);
            int keyLength = Integer.parseInt(parts[2]);
            byte[] salt = Base64.getDecoder().decode(parts[3]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[4]);

            PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                keyLength
            );

            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            return slowEquals(expectedHash, testHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Comparaison lente pour éviter les attaques temporelles.
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}