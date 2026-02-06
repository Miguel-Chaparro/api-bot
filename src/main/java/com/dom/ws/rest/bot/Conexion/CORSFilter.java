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
        // Nota: nginx ya maneja Access-Control-Allow-Origin, 
        // aquí solo agregamos los headers complementarios para CORS
        
        // Solo agregamos estos headers si nginx no los proporciona
        // putSingle() reemplaza si existe, add() agregaría duplicados
        crc1.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        crc1.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        crc1.getHeaders().putSingle("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Api-Key, X-Requested-With");
        crc1.getHeaders().putSingle("Access-Control-Max-Age", "3600");
        crc1.getHeaders().putSingle("Vary", "Origin");
    }
}
