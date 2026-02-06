package com.dom.ws.rest.bot.Listeners;

import com.dom.ws.rest.bot.Conexion.FirebaseInitializer;
import com.google.firebase.FirebaseApp;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Logger;

@WebListener
public class FirebaseContextListener implements ServletContextListener {
    
    private static final Logger log = Logger.getLogger(FirebaseContextListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("=== Inicializando contexto de la aplicación ===");
        try {
            FirebaseInitializer.getInstance().initialize();
            log.info("Firebase inicializado correctamente");
        } catch (Exception e) {
            log.severe("Error al inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("=== Destruyendo contexto de la aplicación ===");
        
        // Limpiar FirebaseApp
        try {
            log.info("Limpiando instancias de FirebaseApp...");
            for (FirebaseApp app : FirebaseApp.getApps()) {
                app.delete();
                log.info("FirebaseApp deletado: " + app.getName());
            }
            log.info("Recursos de Firebase limpiados correctamente");
        } catch (Exception e) {
            log.severe("Error al limpiar recursos de Firebase: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Limpiar JDBC Drivers
        try {
            log.info("Limpiando JDBC Drivers...");
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                    log.info("JDBC Driver deregistrado: " + driver.getClass().getName());
                } catch (SQLException e) {
                    log.warning("Error deregistrando driver " + driver.getClass().getName() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            log.severe("Error limpiando JDBC Drivers: " + e.getMessage());
            e.printStackTrace();
        }
        
        log.info("=== Contexto destruido correctamente ===");
    }
}
