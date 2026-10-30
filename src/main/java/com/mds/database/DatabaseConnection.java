package com.mds.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized database connection manager for MDS (Medical Diagnosis System).
 * Connects to the local MariaDB / MySQL instance in XAMPP.
 * Uses environment variable / system property overrides with default XAMPP credentials.
 */
public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:mariadb://localhost:3306/providencedb";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    // Driver class name for MariaDB Connector/J
    private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.err.println("[MDS Database Error] MariaDB JDBC Driver class not found: " + DRIVER_CLASS);
        }
    }

    private DatabaseConnection() {
        // Utility / Factory class
    }

    /**
     * Resolves the active JDBC URL, checking system properties and environment variables.
     */
    public static String getJdbcUrl() {
        String prop = System.getProperty("mds.db.url");
        if (prop != null && !prop.isBlank()) return prop;
        String env = System.getenv("MDS_DB_URL");
        if (env != null && !env.isBlank()) return env;
        return DEFAULT_URL;
    }

    /**
     * Resolves the active database user.
     */
    public static String getDbUser() {
        String prop = System.getProperty("mds.db.user");
        if (prop != null && !prop.isBlank()) return prop;
        String env = System.getenv("MDS_DB_USER");
        if (env != null && !env.isBlank()) return env;
        return DEFAULT_USER;
    }

    /**
     * Resolves the active database password.
     */
    public static String getDbPassword() {
        String prop = System.getProperty("mds.db.password");
        if (prop != null) return prop;
        String env = System.getenv("MDS_DB_PASSWORD");
        if (env != null) return env;
        return DEFAULT_PASSWORD;
    }

    /**
     * Obtains a fresh JDBC connection to providencedb.
     * Callers must manage the lifecycle via try-with-resources.
     *
     * @return active Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(getJdbcUrl(), getDbUser(), getDbPassword());
        } catch (SQLException e) {
            System.err.println("[MDS Database Error] Failed to connect to database at: " + getJdbcUrl()
                    + " with user: " + getDbUser() + ". Message: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if the MariaDB database is reachable.
     *
     * @return true if connection succeeds, false otherwise
     */
    public static boolean isDatabaseAvailable() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }
}
