package com.dom.ws.rest.bot.Conexion;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper utility for safe database operations with automatic connection management
 * Ensures connections are always properly closed and returned to the pool
 */
public class DatabaseHelper {

    private static final Logger logg = Logger.getLogger(DatabaseHelper.class.getName());

    /**
     * Execute a database operation with automatic connection management
     * Ensures the connection is always closed/returned to the pool
     * Handles connection pool timeouts gracefully
     * 
     * Usage example:
     * DatabaseHelper.withConnection(connection -> {
     *     PreparedStatement ps = connection.prepareStatement(sql);
     *     // ... use the connection
     *     ps.close();
     * });
     */
    public static <T> T withConnection(ConnectionOperation<T> operation) {
        conexionBD conn = null;
        try {
            conn = conexionBD.saberEstado();  // May return instance with null cnn if pool exhausted
            if (conn == null || conn.getCnn() == null) {
                throw new RuntimeException("Connection pool exhausted - all 20 connections busy or database unreachable");
            }
            return operation.execute(conn.getCnn());
        } catch (Exception e) {
            logg.log(Level.SEVERE, "Database operation failed: {0}", e.getMessage());
            throw new RuntimeException("Database operation failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.cerrarConexion();
                } catch (Exception e) {
                    logg.log(Level.SEVERE, "Error closing connection: {0}", e.getMessage());
                }
            }
        }
    }

    /**
     * Execute a database operation without return value
     * Handles connection pool timeouts gracefully
     */
    public static void withConnectionVoid(VoidConnectionOperation operation) {
        conexionBD conn = null;
        try {
            conn = conexionBD.saberEstado();  // May return instance with null cnn if pool exhausted
            if (conn == null || conn.getCnn() == null) {
                throw new RuntimeException("Connection pool exhausted - all 20 connections busy or database unreachable");
            }
            operation.execute(conn.getCnn());
        } catch (Exception e) {
            logg.log(Level.SEVERE, "Database operation failed: {0}", e.getMessage());
            throw new RuntimeException("Database operation failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.cerrarConexion();
                } catch (Exception e) {
                    logg.log(Level.SEVERE, "Error closing connection: {0}", e.getMessage());
                }
            }
        }
    }

    /**
     * Functional interface for operations that return a value
     */
    @FunctionalInterface
    public interface ConnectionOperation<T> {
        T execute(Connection conn) throws Exception;
    }

    /**
     * Functional interface for operations that don't return a value
     */
    @FunctionalInterface
    public interface VoidConnectionOperation {
        void execute(Connection conn) throws Exception;
    }
}
