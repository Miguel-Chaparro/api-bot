package com.dom.ws.rest.bot.Listeners;

import com.dom.ws.rest.bot.Conexion.FirebaseInitializer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class FirebaseContextListener implements ServletContextListener {
    
    private static final Logger log = Logger.getLogger(FirebaseContextListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Inicializando contexto de Firebase...");
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
        log.info("Contexto destruido");
    }
}
