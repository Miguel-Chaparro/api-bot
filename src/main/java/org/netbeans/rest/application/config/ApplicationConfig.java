/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import javax.ws.rs.core.Application;
import com.dom.ws.rest.bot.Conexion.FirebaseInitializer;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author MIGUEL
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // Inicializar Firebase al arrancar la aplicación
        FirebaseInitializer.getInstance().initialize();

        // Registrar todos los recursos y providers en este paquete
        packages("com.dom.ws.rest.bot");
        
        // Registrar manualmente las nuevas clases de recursos si el escaneo de paquetes falla
        // register(com.dom.ws.rest.bot.Services.UserResource.class);
        // register(com.dom.ws.rest.bot.Services.ProjectResource.class); // Y así para las demás
        
        // Registrar el filtro de CORS
        register(com.dom.ws.rest.bot.Conexion.CORSFilter.class);
    }
    
}
