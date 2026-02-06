package com.dom.ws.rest.bot.Conexion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection Pool Manager using HikariCP
 * Provides efficient database connection pooling to avoid creating new connections for each request
 */
public class ConnectionPool {

    private static final Logger logg = Logger.getLogger(ConnectionPool.class.getName());
    private static HikariDataSource dataSource;
    private static ConnectionPool instance;

    private ConnectionPool() {
        initializePool();
    }

    /**
     * Get singleton instance of ConnectionPool
     */
    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * Initialize HikariCP connection pool
     */
    private void initializePool() {
        try {
            String servidor = System.getenv("DB_SERVER");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (servidor == null || user == null || password == null) {
                logg.warning("Database environment variables not found. Connection Pool not initialized.");
                return;
            }

            logg.log(Level.INFO, "=== Inicializando Connection Pool HikariCP ===");
            logg.log(Level.INFO, "DB_SERVER: {0}", servidor);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(servidor);
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");  // Explicitly set MySQL driver
            
            // Pool configuration
            config.setMaximumPoolSize(20);              // Max 20 connections
            config.setMinimumIdle(5);                   // Min 5 idle connections
            config.setConnectionTimeout(30000);         // 30 seconds to get connection
            config.setIdleTimeout(600000);              // 10 minutes idle timeout
            config.setMaxLifetime(1800000);             // 30 minutes max lifetime
            config.setAutoCommit(true);
            config.setLeakDetectionThreshold(60000);    // Log if connection not returned in 60 seconds
            
            // Connection validation
            config.setConnectionTestQuery("SELECT 1");
            
            dataSource = new HikariDataSource(config);
            logg.info("Connection Pool inicializado correctamente - MaxPoolSize: 20, MinIdle: 5");

        } catch (Exception ex) {
            logg.log(Level.SEVERE, "Error inicializando Connection Pool: ", ex);
        }
    }

    /**
     * Get a connection from the pool
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection Pool not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Close the entire pool (call on application shutdown)
     */
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            logg.info("=== Cerrando Connection Pool HikariCP ===");
            dataSource.close();
            logg.info("Connection Pool cerrado correctamente");
        }
    }

    /**
     * Get pool statistics
     */
    public String getPoolStats() {
        if (dataSource == null) {
            return "Connection Pool not initialized";
        }
        return String.format("Active: %d, Idle: %d, Pending: %d, Total: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection(),
                dataSource.getHikariPoolMXBean().getTotalConnections());
    }
}
