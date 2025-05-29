package com.dom.ws.rest.bot.web;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/openapi.json")
public class OpenApiResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOpenApi() {
        try {
            SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                    .resourcePackages(java.util.Collections.singleton("com.dom.ws.rest.bot.Services"));
            OpenApiContext ctx = new JaxrsOpenApiContextBuilder<>()
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
            OpenAPI oas = ctx.read();
            String json = Json.mapper().writeValueAsString(oas);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("No se pudo generar el OpenAPI: " + e.getMessage())
                    .build();
        }
    }
}
