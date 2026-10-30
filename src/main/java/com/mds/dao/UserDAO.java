package com.mds.dao;

import com.mds.database.DatabaseConnection;
import com.mds.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object for user authentication and user accounts.
 * Supports SHA-256 password hashing with demonstration fallback.
 */
public class UserDAO {

    public static final String DEMO_USERNAME = "admin";
    public static final String DEMO_PASSWORD = "1234";

    /**
     * Authenticates a user against the database using SHA-256 password hashing.
     * Retains fallback support for demonstration credentials ('admin' / '1234').
     *
     * @param username user identifier
     * @param rawPassword plain text password entered in form
     * @return true if credentials are valid, false otherwise
     */
    public boolean authenticate(String username, String rawPassword) {
        if (username == null || rawPassword == null) return false;

        String cleanUsername = username.trim();
        String hashedPassword = hashPassword(rawPassword);

        // Attempt database authentication first
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT user_id, username, password_hash, role FROM users WHERE LOWER(username) = LOWER(?) LIMIT 1")) {

            stmt.setString(1, cleanUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (storedHash != null && (storedHash.equalsIgnoreCase(hashedPassword) || storedHash.equals(rawPassword))) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[MDS Auth Notice] Database authentication error: " + e.getMessage());
        }

        // Demonstration fallback: preserves standard demo credentials
        return DEMO_USERNAME.equalsIgnoreCase(cleanUsername) && DEMO_PASSWORD.equals(rawPassword);
    }

    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();

        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE LOWER(username) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("role")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[MDS Auth Error] Failed to find user by username: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Computes the SHA-256 hexadecimal hash of a password string.
     */
    public static String hashPassword(String password) {
        if (password == null) return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
