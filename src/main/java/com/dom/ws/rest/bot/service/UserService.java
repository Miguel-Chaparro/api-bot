package com.dom.ws.rest.bot.service;

import com.dom.ws.rest.bot.DTO.UserDTO;
import java.sql.Timestamp;

public class UserService {
    // Implementa la lógica real de acceso a datos según tu stack (JPA, JDBC, etc.)

    public UserDTO findByEmailOrId(String email, String id) {
        // ...buscar usuario en la BD por email o id...
        // return usuario si existe, si no return null
        return null; // Implementar según tu persistencia
    }

    public UserDTO createInactiveUser(UserDTO input) {
        UserDTO user = new UserDTO();
        user.setId(input.getId());
        user.setEmail(input.getEmail());
        user.setDisplayName(input.getDisplayName());
        user.setPhotoUrl(input.getPhotoUrl());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setProviderId(input.getProviderId());
        user.setCreationTime(new Timestamp(System.currentTimeMillis()));
        user.setLastSignInTime(new Timestamp(System.currentTimeMillis()));
        user.setEmailVerified(false);
        user.setCustomClaims(null);
        user.setDisabled(true); // Estado inactivo
        user.setLastLoginIp(input.getLastLoginIp());
        // ...guardar usuario en la BD...
        return user;
    }
}
