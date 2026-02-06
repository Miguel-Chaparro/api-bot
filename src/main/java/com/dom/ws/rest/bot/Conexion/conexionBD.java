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
 * Wrapper for getting database connections from HikariCP Connection Pool
 * This class maintains backward compatibility while using connection pooling
 * 
 * @author MIGUEL
 */
public class conexionBD {

    private Connection cnn;
    private static conexionBD instance;
    private static final Logger logg = Logger.getLogger(conexionBD.class.getName());

    private conexionBD() {
        logg.log(Level.INFO, "*** Inicio conexionBD ***");
        try {
            // Get connection from HikariCP pool instead of creating a new one
            cnn = ConnectionPool.getInstance().getConnection();
            if (cnn != null) {
                logg.info("Conexion a base de datos obtenida del pool ... Ok");
                logg.info("Pool Status: " + ConnectionPool.getInstance().getPoolStats());
            }
        } catch (SQLException ex) {
            logg.log(Level.SEVERE, "Error getting connection from pool: ", ex);
            cnn = null;
        }
        logg.log(Level.INFO, "*** Fin conexionBD ***");
    }

    /**
     * Get the database connection
     */
    public Connection getCnn() {
        return cnn;
    }

    /**
     * Get or create singleton instance of conexionBD
     */
    public synchronized static conexionBD saberEstado() {
        logg.info("*** Inicio saberEstado ***");
        if (instance == null) {
            instance = new conexionBD();
        }
        logg.info("*** Fin saberEstado ***");
        return instance;
    }

    /**
     * Close the connection (returns it to the pool instead of closing it)
     * HikariCP will manage the actual connection lifecycle
     */
    public void cerrarConexion() {
        logg.info("*** Inicio cerrarConexion ***");
        try {
            if (cnn != null && !cnn.isClosed()) {
                cnn.close(); // Returns connection to pool, doesn't actually close it
                logg.info("Conexion retornada al pool");
            }
        } catch (SQLException ex) {
            logg.log(Level.WARNING, "Error closing connection: ", ex);
        }
        instance = null;
        logg.info("*** Fin cerrarConexion ***");
    }
}
