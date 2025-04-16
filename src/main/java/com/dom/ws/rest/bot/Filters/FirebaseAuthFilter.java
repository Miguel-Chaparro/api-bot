package com.dom.ws.rest.bot.Filters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class FirebaseAuthFilter implements ContainerRequestFilter {
    
    private static final Logger log = Logger.getLogger(FirebaseAuthFilter.class.getName());
    private static final String API_KEY_HEADER = "x-api-key";
    private static final String ENV_API_KEY = System.getenv("API_KEY");

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Primero verificar si hay una API Key
        String apiKey = requestContext.getHeaderString(API_KEY_HEADER);
        if (apiKey != null && ENV_API_KEY != null && ENV_API_KEY.equals(apiKey)) {
            log.info("Autenticación exitosa usando API Key");
            return;
        }

        // Si no hay API Key válida, verificar token de Firebase
        String authHeader = requestContext.getHeaderString("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warning("No se encontró autorización válida");
            requestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Se requiere autenticación (Firebase token o API Key)")
                .build());
            return;
        }

        String token = authHeader.substring(7);
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            log.info("Token verificado para usuario: " + decodedToken.getUid());
            requestContext.setProperty("user", decodedToken);
        } catch (Exception e) {
            log.severe("Error validando token: " + e.getMessage());
            requestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Token inválido o expirado")
                .build());
        }
    }
}