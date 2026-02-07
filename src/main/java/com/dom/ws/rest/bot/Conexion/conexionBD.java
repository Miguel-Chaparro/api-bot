/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for database operations using HikariCP Connection Pool
 * Each operation gets a fresh connection from the pool and closes it after use
 * 
 * @author MIGUEL
 */
public class conexionBD {

    private Connection cnn;
    private static final Logger logg = Logger.getLogger(conexionBD.class.getName());
    private boolean isFromPool = false;

    /**
     * Constructor - gets a fresh connection from the pool
     */
    private conexionBD() {
        logg.log(Level.INFO, "*** Inicio conexionBD ***");
        obtenerConexion();
        logg.log(Level.INFO, "*** Fin conexionBD ***");
    }

    /**
     * Internal method to get connection from pool
     */
    private void obtenerConexion() {
        try {
            cnn = ConnectionPool.getInstance().getConnection();
            isFromPool = true;
            if (cnn != null) {
                logg.info("Conexion a base de datos obtenida del pool ... Ok");
                logg.info("Pool Status: " + ConnectionPool.getInstance().getPoolStats());
            }
        } catch (SQLException ex) {
            logg.log(Level.SEVERE, "ERROR - CRITICAL: Connection pool EXHAUSTED! All 20 connections busy or timeout. Database unreachable. Error: {0}", ex.getMessage());
            logg.log(Level.SEVERE, "This usually means connections are not being properly closed. Check for connection leaks in DAO methods.", ex);
            cnn = null;
            isFromPool = false;
            // DO NOT throw - let caller deal with null connection
        }
    }

    /**
     * Get the database connection
     */
    public Connection getCnn() {
        return cnn;
    }

    /**
     * Create a NEW connection instance for each operation
     * This ensures fresh connections and prevents connection reuse issues
     */
    public synchronized static conexionBD saberEstado() {
        logg.info("*** Inicio saberEstado ***");
        conexionBD conexion = new conexionBD();
        logg.info("*** Fin saberEstado ***");
        return conexion;
    }

    /**
     * Close the connection and return it to the pool
     * Should be called after each database operation completes
     */
    public void cerrarConexion() {
        logg.info("*** Inicio cerrarConexion ***");
        try {
            if (cnn != null && !cnn.isClosed()) {
                cnn.close(); // Returns connection to pool (HikariCP intercepts this)
                logg.info("Conexion retornada al pool");
            }
        } catch (SQLException ex) {
            logg.log(Level.WARNING, "Error closing connection: ", ex);
        } finally {
            cnn = null;
            isFromPool = false;
        }
        logg.info("*** Fin cerrarConexion ***");
    }
}
