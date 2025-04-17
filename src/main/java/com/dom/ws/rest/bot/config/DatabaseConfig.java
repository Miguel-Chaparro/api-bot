package com.dom.ws.rest.bot.config;

import com.dom.ws.rest.bot.Conexion.conexionBD;
import java.sql.Connection;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger log = Logger.getLogger(DatabaseConfig.class.getName());
    private static DatabaseConfig instance;
    private final conexionBD connection;

    private DatabaseConfig() {
        this.connection = conexionBD.saberEstado();
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection.getCnn();
    }

    public void closeConnection() {
        connection.cerrarConexion();
    }
}