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

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warning("No authorization header found");
            requestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("No se proporcionó token de autenticación")
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