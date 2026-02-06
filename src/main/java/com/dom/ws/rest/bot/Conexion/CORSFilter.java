/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Conexion;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author MIGUEL
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException {
        String origin = crc.getHeaderString("Origin");
        
        // Orígenes permitidos
        String[] allowedOrigins = {
            "https://dashboard.dommatos.com",
            "http://dashboard.dommatos.com",
            "https://customer.dommatos.com",
            "http://customer.dommatos.com",
            "http://localhost:3040",
            "http://localhost:3039",
            "http://localhost:3000"
        };
        
        boolean isOriginAllowed = false;
        if (origin != null && !origin.isEmpty()) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equalsIgnoreCase(origin)) {
                    isOriginAllowed = true;
                    break;
                }
            }
        }
        
        // Usar putSingle para reemplazar el header en lugar de agregarlo
        // Esto evita duplicados cuando nginx también agrega el header
        if (isOriginAllowed) {
            crc1.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
        }
        
        // Estos headers no son duplicables, usar putSingle
        crc1.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        crc1.getHeaders().putSingle("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Api-Key, X-Requested-With");
        crc1.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        crc1.getHeaders().putSingle("Access-Control-Max-Age", "3600");
        crc1.getHeaders().putSingle("Vary", "Origin");
    }
}
