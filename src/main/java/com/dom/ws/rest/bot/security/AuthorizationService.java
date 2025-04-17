package com.dom.ws.rest.bot.security;

import com.dom.ws.rest.bot.exception.ApiException;
import com.dom.ws.rest.bot.service.impl.UserServiceImpl;
import com.dom.ws.rest.bot.service.interfaces.UserService;
import com.google.firebase.auth.FirebaseToken;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.logging.Logger;

public class AuthorizationService {
    private static final Logger log = Logger.getLogger(AuthorizationService.class.getName());
    private final UserService userService;

    public AuthorizationService() {
        this.userService = new UserServiceImpl();
    }

    public void validateAdminAccess(ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            throw new ApiException("No autorizado", 401);
        }

        if (!userService.isAdmin(decodedToken.getUid())) {
            throw new ApiException("Acceso denegado. Se requiere perfil de administrador", 403);
        }
    }

    public FirebaseToken validateAuthentication(ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            throw new ApiException("No autorizado", 401);
        }
        return decodedToken;
    }
}