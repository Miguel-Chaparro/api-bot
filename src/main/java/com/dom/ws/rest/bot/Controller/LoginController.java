package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.service.UserService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginController {

    private UserService userService = new UserService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDTO userInput) {
        // Buscar usuario por email o id
        UserDTO user = userService.findByEmailOrId(userInput.getEmail(), userInput.getId());

        if (user != null) {
            if (!user.isDisabled()) {
                return Response.ok()
                    .entity(new LoginResponse("login exitoso", user))
                    .build();
            } else {
                return Response.ok()
                    .entity(new LoginResponse("usuario inactivo", user))
                    .build();
            }
        } else {
            // Crear usuario inactivo
            UserDTO newUser = userService.createInactiveUser(userInput);
            return Response.ok()
                .entity(new LoginResponse("usuario inactivo", newUser))
                .build();
        }
    }

    // Clase interna para respuesta
    public static class LoginResponse {
        public String status;
        public UserDTO user;
        public LoginResponse(String status, UserDTO user) {
            this.status = status;
            this.user = user;
        }
    }
}
