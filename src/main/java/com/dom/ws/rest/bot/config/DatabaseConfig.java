package com.dom.ws.rest.bot.config;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import java.sql.Connection;

/**
 * DEPRECATED: This class should not be used.
 * Always use DatabaseHelper or obtain fresh connections in DAOs instead.
 * 
 * Keeping for backward compatibility but refactored to avoid connection leaks.
 * Never hold connections as instance variables - always get fresh from pool per operation.
 */
@Deprecated
public class DatabaseConfig {
    private static DatabaseConfig instance;

    private DatabaseConfig() {
        // Empty - do not hold connections here
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * DEPRECATED - This method is unsafe and should not be used.
     * Always use DatabaseHelper.withConnection() instead.
     * 
     * @deprecated Use DatabaseHelper.withConnection() for safe connection management
     * @return a fresh connection from the pool (MUST be closed by caller)
     */
    @Deprecated
    public Connection getConnection() {
        try {
            conexionBD con = conexionBD.saberEstado();
            return con != null ? con.getCnn() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method should never be called - use DatabaseHelper instead.
     * @deprecated This method is unsafe
     */
    @Deprecated
    public void closeConnection() {
        // This is unsafe - connections should be managed in try-finally blocks
        throw new UnsupportedOperationException(
            "DatabaseConfig is deprecated. Use DatabaseHelper.withConnection() instead.");
    }
}
