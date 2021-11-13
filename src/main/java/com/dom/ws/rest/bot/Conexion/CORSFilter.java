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
        crc1.getHeaders().add("Access-Control-Allow-Origin", "*");
        crc1.getHeaders().add("Access-Control-Allow-Headers","*");
        //crc1.getHeaders().add("Access-Control-Allow-Credentials", "true");
        crc1.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
