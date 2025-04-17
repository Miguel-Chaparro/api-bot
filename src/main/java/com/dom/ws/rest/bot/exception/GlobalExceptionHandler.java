package com.dom.ws.rest.bot.exception;

import com.dom.ws.rest.bot.web.ApiResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {
    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        log.log(Level.SEVERE, "Error no manejado", exception);

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            return Response.status(getStatusCode(apiException.getCode()))
                .entity(ApiResponse.error(apiException.getMessage(), apiException.getCode()))
                .build();
        }

        // Para otras excepciones, devolver error interno del servidor
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(ApiResponse.error("Error interno del servidor", -1))
            .build();
    }

    private Response.Status getStatusCode(int code) {
        switch (code) {
            case 401:
                return Response.Status.UNAUTHORIZED;
            case 403:
                return Response.Status.FORBIDDEN;
            case 404:
                return Response.Status.NOT_FOUND;
            case 400:
                return Response.Status.BAD_REQUEST;
            default:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}